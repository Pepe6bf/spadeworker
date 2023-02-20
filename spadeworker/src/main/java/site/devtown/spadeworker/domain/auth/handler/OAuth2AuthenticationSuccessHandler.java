package site.devtown.spadeworker.domain.auth.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;
import site.devtown.spadeworker.domain.auth.model.info.OAuth2UserInfo;
import site.devtown.spadeworker.domain.auth.model.info.OAuth2UserInfoFactory;
import site.devtown.spadeworker.domain.auth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import site.devtown.spadeworker.domain.auth.repository.UserRefreshTokenRepository;
import site.devtown.spadeworker.domain.auth.token.AuthToken;
import site.devtown.spadeworker.domain.auth.token.AuthTokenProvider;
import site.devtown.spadeworker.domain.auth.token.UserRefreshToken;
import site.devtown.spadeworker.domain.user.model.constant.AuthProviderType;
import site.devtown.spadeworker.domain.user.model.constant.UserRoleType;
import site.devtown.spadeworker.domain.user.model.entity.Role;
import site.devtown.spadeworker.domain.user.repository.RoleRepository;
import site.devtown.spadeworker.global.util.CookieUtil;

import javax.persistence.EntityNotFoundException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static site.devtown.spadeworker.domain.auth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository.REDIRECT_URI_PARAM_COOKIE_NAME;
import static site.devtown.spadeworker.domain.auth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository.REFRESH_TOKEN;

@Slf4j
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler
        extends SimpleUrlAuthenticationSuccessHandler {

    private final AuthTokenProvider jwtProvider;
    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;
    private final RoleRepository roleRepository;
    private final List<String> authorizedRedirectUris;
    private final Integer cookieMaxAge;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        // 응답 커밋 여부 체크
        if (response.isCommitted()) {
            logger.debug("응답이 이미 커밋되었으므로 [" + targetUrl + "]로 리다이렉션 할 수 없습니다.");
            return;
        }

        clearAuthenticationAttributes(request, response);
        // 리다이렉트 수행
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    /**
     * 토큰 생성 후 클라이언트에게 리다이렉트를 진행하는 메인 로직
     */
    protected String determineTargetUrl(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        Optional<String> redirectUri = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        // 허용된 리다이렉션 URI 인지 검증
        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new IllegalArgumentException("허용되지 않은 리다이렉션 URI 입니다.");
        }

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        AuthProviderType providerType = AuthProviderType.valueOf(authToken.getAuthorizedClientRegistrationId().toUpperCase());

        OidcUser user = ((OidcUser) authentication.getPrincipal());
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, user.getAttributes());
        Collection<? extends GrantedAuthority> authorities = ((OidcUser) authentication.getPrincipal()).getAuthorities();

        // TODO : 해당 로직은 USER || ADMIN 권한만 추출 할 수 있음. 향후 권한이 추가될 경우 리펙토링이 필요함.
        Role role = roleRepository.findByRoleType(
                hasAuthority(authorities, UserRoleType.ADMIN.getCode()) ? UserRoleType.ADMIN : UserRoleType.USER
        ).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 권한입니다."));

        // access token 생성
        AuthToken accessToken = jwtProvider.createAccessToken(
                userInfo.getId(),
                List.of(role.getRoleType())
        );

        log.info("[Access-Token] = {}", accessToken.getTokenValue());

        // refresh 토큰 생성
        AuthToken refreshToken = jwtProvider.createRefreshToken(
                userInfo.getId(),
                List.of(role.getRoleType())
        );

        // refresh 토큰 DB에 저장
        Optional<UserRefreshToken> userRefreshToken = userRefreshTokenRepository.findByPersonalId(userInfo.getId());
        if (userRefreshToken.isPresent()) {
            userRefreshToken.get().changeTokenValue(refreshToken.getTokenValue());
        } else {
            userRefreshTokenRepository.saveAndFlush(
                    UserRefreshToken.of(
                            userInfo.getId(),
                            refreshToken.getTokenValue()
                    )
            );
        }

        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
        CookieUtil.addCookie(
                response,
                REFRESH_TOKEN,
                refreshToken.getTokenValue(),
                cookieMaxAge
        );

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", accessToken.getTokenValue())
                .build().toUriString();
    }

    /**
     * 인가된 리다이렉트 URI 가 맞는지 검증
     */
    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return authorizedRedirectUris
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    // Host & Port 만 검증함
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    return authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort();
                });
    }

    /**
     * 권한을 가지고 있는지 체크
     */
    private boolean hasAuthority(
            Collection<? extends GrantedAuthority> authorities,
            String authority
    ) {
        if (authorities == null) {
            return false;
        }

        for (GrantedAuthority grantedAuthority : authorities) {
            if (authority.equals(grantedAuthority.getAuthority())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 인증 속성 초기화
     */
    protected void clearAuthenticationAttributes(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}

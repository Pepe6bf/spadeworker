package site.devtown.spadeworker.global.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.HandlerExceptionResolver;
import site.devtown.spadeworker.domain.auth.handler.OAuth2AuthenticationFailureHandler;
import site.devtown.spadeworker.domain.auth.handler.OAuth2AuthenticationSuccessHandler;
import site.devtown.spadeworker.domain.auth.handler.TokenAccessDeniedHandler;
import site.devtown.spadeworker.domain.auth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import site.devtown.spadeworker.domain.auth.repository.UserRefreshTokenRepository;
import site.devtown.spadeworker.domain.auth.service.JwtService;
import site.devtown.spadeworker.domain.user.repository.RoleRepository;

import java.util.List;

@RequiredArgsConstructor
@PropertySource(
        value = "classpath:/oauth.properties"
)
@Configuration
public class OAuth2Config {

    private final JwtService jwtService;
    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final RoleRepository roleRepository;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Value("${oauth2.authorized-redirect-uris}")
    private List<String> authorizedRedirectUris;
    @Value("${oauth2.cookie-max-age}")
    private Integer cookieMaxAge;

    /*
     * 쿠키 기반 인가 Repository
     * 인가 응답을 연계 하고 검증할 때 사용.
     * */
    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    /*
     * Oauth 인증 성공 핸들러
     * */
    @Bean
    public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return new OAuth2AuthenticationSuccessHandler(
                jwtService,
                userRefreshTokenRepository,
                oAuth2AuthorizationRequestBasedOnCookieRepository(),
                roleRepository,
                authorizedRedirectUris,
                cookieMaxAge
        );
    }

    /*
     * Oauth 인증 실패 핸들러
     * */
    @Bean
    public OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
        return new OAuth2AuthenticationFailureHandler(
                oAuth2AuthorizationRequestBasedOnCookieRepository()
        );
    }

    /**
     * 토큰 접근 거부 핸들러
     */
    @Bean
    public TokenAccessDeniedHandler tokenAccessDeniedHandler() {
        return new TokenAccessDeniedHandler(
                handlerExceptionResolver
        );
    }
}

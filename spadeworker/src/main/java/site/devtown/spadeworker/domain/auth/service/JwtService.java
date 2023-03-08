package site.devtown.spadeworker.domain.auth.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.devtown.spadeworker.domain.auth.exception.InvalidTokenException;
import site.devtown.spadeworker.domain.auth.model.UserPrincipal;
import site.devtown.spadeworker.domain.auth.repository.UserRefreshTokenRepository;
import site.devtown.spadeworker.domain.auth.token.AuthToken;
import site.devtown.spadeworker.domain.auth.token.AuthTokenProvider;
import site.devtown.spadeworker.domain.auth.token.UserRefreshToken;
import site.devtown.spadeworker.domain.user.model.entity.User;
import site.devtown.spadeworker.domain.user.repository.UserRepository;
import site.devtown.spadeworker.global.exception.ResourceNotFoundException;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import static site.devtown.spadeworker.domain.auth.exception.AuthExceptionCode.INVALID_REFRESH_TOKEN;
import static site.devtown.spadeworker.domain.user.exception.UserExceptionCode.USER_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class JwtService {

    private final UserRepository userRepository;
    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final AuthTokenProvider tokenProvider;

    /**
     * Access Token 생성
     */
    public AuthToken createAccessToken(
            String personalId,
            Collection<? extends GrantedAuthority> roles
    ) {
        return tokenProvider.generateAccessToken(personalId, roles);
    }

    /**
     * Refresh Token 생성
     */
    public AuthToken createRefreshToken(
            String personalId,
            Collection<? extends GrantedAuthority> roles
    ) {
        return tokenProvider.generateRefreshToken(personalId, roles);
    }

    /**
     * refresh token 확인 후 토큰 전체 재발급
     */
    @Transactional
    public Map<String, String> reissueToken(
            String expiredAccessTokenValue,
            String refreshTokenValue
    ) {
        AuthToken expiredAccessToken = createAuthTokenFromAccessTokenValue(expiredAccessTokenValue);
        // 만료된 토큰의 Claim 조회
        Claims expiredTokenClaims = expiredAccessToken.getExpiredTokenClaims();

        String userId = expiredTokenClaims.getSubject();
        Collection<? extends GrantedAuthority> roles = getUserAuthority(expiredTokenClaims.get("roles", String.class));

        // 리프레쉬 토큰 파싱
        AuthToken refreshToken = createAuthTokenFromRefreshTokenValue(refreshTokenValue);

        // refreshToken 검증
        try {
            refreshToken.validate();
        } catch (InvalidTokenException e) {
            throw new InvalidTokenException(INVALID_REFRESH_TOKEN);
        }

        // userId refresh token 으로 DB 확인
        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByPersonalIdAndTokenValue(
                userId,
                refreshTokenValue
        ).orElseThrow(() -> new InvalidTokenException(INVALID_REFRESH_TOKEN));

        // 새로운 access token 발급
        AuthToken newAccessToken = createAccessToken(userId, roles);

        // Refresh Token Rotation
        // refresh 토큰 설정
        AuthToken newRefreshToken = createRefreshToken(userId, roles);
        // DB에 refresh 토큰 업데이트
        userRefreshToken.changeTokenValue(newRefreshToken.getTokenValue());

        return Map.of(
                "accessToken", newAccessToken.getTokenValue(),
                "refreshToken", newRefreshToken.getTokenValue()
        );
    }

    /**
     * 로그아웃 구현을 위해 리프레쉬 토큰 제거
     */
    @Transactional
    public void deleteRefreshToken(String accessToken) {
        AuthToken accessAuthToken = createAuthTokenFromAccessTokenValue(accessToken);
        String userPersonalId = accessAuthToken.getTokenClaims().getSubject();

        // refresh token 삭제
        userRefreshTokenRepository.deleteAllByPersonalId(userPersonalId);
    }

    /**
     * JWT 값을 기반으로 사용자 인가 조회
     */
    @Transactional(readOnly = true)
    public Authentication getAuthentication(AuthToken accessToken) {
        // access token 검증
        accessToken.validate();

        // access token claims 조회
        Claims claims = accessToken.getTokenClaims();

        // claims 값을 기반으로 사용자 Entity 조회
        User user = userRepository.findByPersonalId(claims.getSubject())
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));

        // claims 값을 기반으로 사용자의 권한 조회
        Collection<? extends GrantedAuthority> roles = getUserAuthority(claims.get("roles", String.class));
        UserPrincipal userPrincipal = UserPrincipal.from(user, roles);

        return new UsernamePasswordAuthenticationToken(
                userPrincipal,
                accessToken,
                userPrincipal.getAuthorities()
        );
    }

    /**
     * access-token 문자열 값을 기반으로 AuthToken 객체 생성
     */
    public AuthToken createAuthTokenFromAccessTokenValue(String accessToken) {
        return tokenProvider.convertAccessTokenToAuthToken(accessToken);
    }

    /**
     * refresh-token 문자열 값을 기반으로 AuthToken 객체 생성
     */
    public AuthToken createAuthTokenFromRefreshTokenValue(String refreshToken) {
        return tokenProvider.convertRefreshTokenToAuthToken(refreshToken);
    }

    // 토큰에서 파싱한 사용자 권한 리스트 문자열을 각각 분리해서 GrantedAuthority List 로 반환
    private Collection<? extends GrantedAuthority> getUserAuthority(String userRoles) {
        return Arrays.stream(userRoles.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
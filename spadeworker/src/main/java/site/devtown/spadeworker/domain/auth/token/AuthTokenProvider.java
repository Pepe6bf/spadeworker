package site.devtown.spadeworker.domain.auth.token;

import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import site.devtown.spadeworker.domain.user.model.constant.UserRoleType;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Slf4j
public class AuthTokenProvider {

    private final Key accessTokenSecretKey;
    private final Key refreshTokenSecretKey;
    private final Date accessTokenExpiry;
    private final Date refreshTokenExpiry;

    public AuthTokenProvider(
            String accessTokenSecretKey,
            long accessTokenExpiry,
            String refreshTokenSecretKey,
            long refreshTokenExpiry

    ) {
        this.accessTokenSecretKey = Keys.hmacShaKeyFor(accessTokenSecretKey.getBytes(StandardCharsets.UTF_8));
        this.refreshTokenSecretKey = Keys.hmacShaKeyFor(refreshTokenSecretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpiry = new Date(new Date().getTime() + accessTokenExpiry);
        this.refreshTokenExpiry = new Date(new Date().getTime() + refreshTokenExpiry);
    }

    /**
     * Access Token 생성
     */
    public AuthToken generateAccessToken(
            String personalId,
            List<UserRoleType> roles
    ) {
        return AuthToken.of(
                personalId,
                roles,
                accessTokenExpiry,
                accessTokenSecretKey
        );
    }

    /**
     * Refresh Token 생성
     */
    public AuthToken generateRefreshToken(
            String personalId,
            List<UserRoleType> roles
    ) {
        return AuthToken.of(
                personalId,
                roles,
                refreshTokenExpiry,
                refreshTokenSecretKey
        );
    }

    /**
     * Access Token 값을 기반으로 AuthToken 객체 생성
     */
    public AuthToken convertAccessTokenToAuthToken(String accessToken) {
        return AuthToken.of(accessToken, accessTokenSecretKey);
    }

    /**
     * Refresh Token 값을 기반으로 AuthToken 객체 생성
     */
    public AuthToken convertRefreshTokenToAuthToken(String refreshToken) {
        return AuthToken.of(refreshToken, refreshTokenSecretKey);
    }
}
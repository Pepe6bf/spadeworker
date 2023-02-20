package site.devtown.spadeworker.domain.auth.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import site.devtown.spadeworker.domain.auth.exception.TokenValidFailedException;
import site.devtown.spadeworker.domain.user.model.constant.UserRoleType;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    public AuthToken createAccessToken(
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
    public AuthToken createRefreshToken(
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
    public AuthToken convertAccessToken(String accessToken) {
        return AuthToken.of(accessToken, accessTokenSecretKey);
    }

    /**
     * Refresh Token 값을 기반으로 AuthToken 객체 생성
     */
    public AuthToken convertRefreshToken(String refreshToken) {
        return AuthToken.of(refreshToken, refreshTokenSecretKey);
    }

    /**
     * 토큰으로 사용자 인가 조회
     */
    public Authentication getAuthentication(AuthToken token) {
        if (!token.validate()) {
            throw new TokenValidFailedException();
        }

        Claims claims = token.getTokenClaims();
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(new String[]{claims.get("roles").toString()})
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        User userPrincipal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(
                userPrincipal,
                token,
                userPrincipal.getAuthorities()
        );
    }
}
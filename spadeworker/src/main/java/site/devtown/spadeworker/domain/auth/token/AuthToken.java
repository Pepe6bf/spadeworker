package site.devtown.spadeworker.domain.auth.token;

import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import site.devtown.spadeworker.domain.auth.exception.InvalidTokenException;
import site.devtown.spadeworker.domain.auth.exception.NotExpiredTokenException;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import static site.devtown.spadeworker.domain.auth.exception.AuthExceptionCode.*;

@Slf4j
public class AuthToken {
    @Getter
    private final String tokenValue;
    private final Key key;

    private AuthToken(
            String personalId,
            Collection<? extends GrantedAuthority> roles,
            Date expiry,
            Key key
    ) {
        this.key = key;
        this.tokenValue = generateAccessTokenValue(personalId, roles, expiry);
    }

    public static AuthToken of(
            String personalId,
            Collection<? extends GrantedAuthority> roles,
            Date expiry,
            Key key
    ) {
        return new AuthToken(personalId, roles, expiry, key);
    }

    private AuthToken(
            String tokenValue,
            Key key
    ) {
        this.tokenValue = tokenValue;
        this.key = key;
    }

    public static AuthToken of(
            String tokenValue,
            Key key
    ) {
        return new AuthToken(tokenValue, key);
    }

    /**
     * Token 값 생성
     */
    private String generateAccessTokenValue(
            String personalId,
            Collection<? extends GrantedAuthority> userRoles,
            Date expiry
    ) {
        String roles = userRoles.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Claims claims = Jwts.claims()
                .setSubject(personalId)  // 토큰 제목 설정
                .setIssuedAt(new Date(System.currentTimeMillis()))  // 토큰 생성일 설정
                .setExpiration(expiry);  // 토큰 만료일 설정
        claims.put("roles", roles);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Token Claim 조회
     */
    public Claims getTokenClaims() {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(tokenValue)
                    .getBody();
        } catch (SecurityException e) {
            throw new InvalidTokenException(INVALID_TOKEN_SIGNATURE);
        } catch (ExpiredJwtException e) {
            throw new InvalidTokenException(EXPIRED_TOKEN, e.getClaims());
        } catch (UnsupportedJwtException e) {
            throw new InvalidTokenException(UNSUPPORTED_TOKEN);
        } catch (JwtException e) {
            throw new InvalidTokenException(INVALID_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new InvalidTokenException(INVALID_TOKEN_COMPACT);
        }
    }

    /**
     * Access Token 이 만료될 경우, 재발급을 위해 만료된 토큰을 조회.
     */
    public Claims getExpiredTokenClaims() {
        try {
            getTokenClaims();
        } catch (InvalidTokenException e) {
            if (!e.getAuthExceptionCode().equals(EXPIRED_TOKEN)) {
                throw e;
            }
            return e.getExpiredTokenClaims();
        }

        throw new NotExpiredTokenException();
    }

    /**
     * 토큰 유효성 검증
     */
    public boolean validate() {
        return getTokenClaims() != null;
    }
}

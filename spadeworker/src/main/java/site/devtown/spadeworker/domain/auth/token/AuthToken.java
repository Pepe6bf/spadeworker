package site.devtown.spadeworker.domain.auth.token;

import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import site.devtown.spadeworker.domain.user.model.constant.UserRoleType;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Slf4j
public class AuthToken {

    @Getter
    private final String tokenValue;
    private final Key key;

    private AuthToken(
            String personalId,
            List<UserRoleType> roles,
            Date expiry,
            Key key
    ) {
        this.key = key;
        this.tokenValue = generateAccessTokenValue(personalId, roles, expiry);
    }

    public static AuthToken of(
            String personalId,
            List<UserRoleType> roles,
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
            List<UserRoleType> roles,
            Date expiry
    ) {

        Claims claims = Jwts.claims()
                .setSubject(personalId) // 토큰 제목 설정
                .setIssuedAt(new Date(System.currentTimeMillis()))    // 토큰 생성일 설정
                .setExpiration(expiry);// 토큰 만료일 설정
        claims.put("roles", roles);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
}

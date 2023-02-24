package site.devtown.spadeworker.domain.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.devtown.spadeworker.domain.auth.exception.ExpiredTokenException;
import site.devtown.spadeworker.domain.auth.exception.NotExpiredTokenException;
import site.devtown.spadeworker.domain.auth.exception.TokenValidFailedException;
import site.devtown.spadeworker.domain.auth.repository.UserRefreshTokenRepository;
import site.devtown.spadeworker.domain.auth.token.AuthToken;
import site.devtown.spadeworker.domain.auth.token.AuthTokenProvider;
import site.devtown.spadeworker.domain.auth.token.UserRefreshToken;
import site.devtown.spadeworker.domain.user.model.constant.UserRoleType;

import java.util.List;
import java.util.Map;

import static site.devtown.spadeworker.domain.auth.exception.AuthExceptionCode.INVALID_REFRESH_TOKEN;
import static site.devtown.spadeworker.domain.auth.exception.AuthExceptionCode.INVALID_TOKEN;

@RequiredArgsConstructor
@Service
public class JwtService {

    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final AuthTokenProvider tokenProvider;

    /**
     * refresh token 확인 후 토큰 전체 재발급
     */
    @Transactional
    public Map<String, String> tokenReissue(
            String accessToken,
            String refreshToken
    ) {
        AuthToken authToken = tokenProvider.convertAccessToken(accessToken);
        Claims claims = getTokenClaims(authToken);

        String userId = claims.getSubject();
        UserRoleType roleType = UserRoleType.of(claims.get("roles", String.class));

        AuthToken authRefreshToken = tokenProvider.convertRefreshToken(refreshToken);

        // refreshToken 검증
        validateRefreshToken(authRefreshToken);

        // userId refresh token 으로 DB 확인
        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByPersonalIdAndTokenValue(
                userId,
                refreshToken
        ).orElseThrow(() -> new TokenValidFailedException(INVALID_REFRESH_TOKEN.getMessage()));

        AuthToken newAccessToken = tokenProvider.createAccessToken(userId, List.of(roleType));

        // Refresh Token Rotation
        // refresh 토큰 설정
        AuthToken newRefreshToken = tokenProvider.createRefreshToken(userId, List.of(roleType));
        // DB에 refresh 토큰 업데이트
        userRefreshToken.changeTokenValue(newRefreshToken.getTokenValue());

        return Map.of(
                "accessToken", newAccessToken.getTokenValue(),
                "refreshToken", newRefreshToken.getTokenValue()
        );
    }

    private Claims getTokenClaims(AuthToken token) {
        Claims claims = null;
        try {
            token.validate();
        } catch (ExpiredJwtException e) {
            claims = token.getExpiredTokenClaims();
        }

        if (claims == null) {
            throw new NotExpiredTokenException();
        }

        return claims;
    }

    private void validateRefreshToken(AuthToken token) {
        try {
            token.validate();
        } catch (TokenValidFailedException e) {
            throw new TokenValidFailedException(INVALID_REFRESH_TOKEN.getMessage());
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException(INVALID_REFRESH_TOKEN.getMessage());
        }
    }
}

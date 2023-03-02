package site.devtown.spadeworker.domain.auth.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import site.devtown.spadeworker.domain.auth.exception.TokenValidFailedException;
import site.devtown.spadeworker.domain.auth.service.JwtService;
import site.devtown.spadeworker.domain.auth.token.AuthToken;
import site.devtown.spadeworker.global.util.HeaderUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static site.devtown.spadeworker.domain.auth.exception.AuthExceptionCode.*;

/**
 * 사용자 인증 시 Token 을 검증하는 Servlet Filter
 */
@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // access token str 파싱
        String accessTokenValue = HeaderUtil.getAccessToken(request);

        // 토큰이 없을 경우
        if (accessTokenValue == null) {
            request.setAttribute("exception", NO_TOKEN.getCode());
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 값을 AuthToken 객체로 변환
        AuthToken accessAuthToken = jwtService.createAuthTokenFromAccessTokenValue(accessTokenValue);

        // token 을 재발급 하는 경우
        String path = request.getRequestURI();
        if ("/api/auth/refresh".equals(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if (accessAuthToken.validate()) {
                Authentication authentication = jwtService.getAuthentication(accessAuthToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (ExpiredJwtException e) {
            request.setAttribute("exception", EXPIRED_TOKEN.getCode());
        } catch (JwtException e) {
            request.setAttribute("exception", INVALID_TOKEN.getCode());
        } catch (TokenValidFailedException e) {
            request.setAttribute("exception", AUTHENTICATION_CLIENT_EXCEPTION.getCode());
        }

        filterChain.doFilter(request, response);
    }
}

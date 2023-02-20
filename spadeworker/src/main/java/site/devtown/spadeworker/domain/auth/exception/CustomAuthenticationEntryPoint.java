package site.devtown.spadeworker.domain.auth.exception;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import site.devtown.spadeworker.global.exception.ExceptionResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationEntryPoint
        implements AuthenticationEntryPoint {
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(AuthExceptionCode.INVALID_TOKEN.getHttpStatus().value());
        response.getWriter().write(
                ExceptionResponse.of(AuthExceptionCode.INVALID_TOKEN)
                        .convertJson()
        );
    }
}
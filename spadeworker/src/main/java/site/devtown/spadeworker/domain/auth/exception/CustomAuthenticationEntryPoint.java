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

    private static final String exceptionAttributeName = "exceptionCode";

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {
        setResponse(response, (AuthExceptionCode) request.getAttribute(exceptionAttributeName));
    }

    // 한글 출력을 위해 getWriter() 사용
    private void setResponse(
            HttpServletResponse response,
            AuthExceptionCode exceptionCode
    ) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().print(ExceptionResponse.of(exceptionCode).convertJson());
    }
}
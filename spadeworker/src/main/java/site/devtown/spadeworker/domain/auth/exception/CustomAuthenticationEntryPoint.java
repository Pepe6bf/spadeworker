package site.devtown.spadeworker.domain.auth.exception;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import site.devtown.spadeworker.global.exception.ExceptionResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static site.devtown.spadeworker.domain.auth.exception.AuthExceptionCode.*;

public class CustomAuthenticationEntryPoint
        implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {
        String exception = (String) request.getAttribute("exception");

        if (exception == null)
            exception = "NO_EXCEPTION";

        if (exception.equals(NO_TOKEN.getCode())) {
            setResponse(response, NO_TOKEN);
        } else if (exception.equals(EXPIRED_TOKEN.getCode())) {
            setResponse(response, EXPIRED_TOKEN);
        } else if (exception.equals(INVALID_TOKEN.getCode())) {
            setResponse(response, INVALID_TOKEN);
        } else if (exception.equals(AUTHENTICATION_CLIENT_EXCEPTION.getCode())) {
            setResponse(response, AUTHENTICATION_CLIENT_EXCEPTION);
        }
    }

    //한글 출력을 위해 getWriter() 사용
    private void setResponse(HttpServletResponse response, AuthExceptionCode exceptionCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ExceptionResponse errorResponse = ExceptionResponse.of(exceptionCode);
        response.getWriter().print(errorResponse.convertJson());
    }
}
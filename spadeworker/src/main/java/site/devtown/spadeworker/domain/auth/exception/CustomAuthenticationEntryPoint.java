package site.devtown.spadeworker.domain.auth.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import site.devtown.spadeworker.global.exception.ExceptionResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static site.devtown.spadeworker.domain.auth.exception.AuthExceptionCode.AUTHENTICATION_ERROR;

@Slf4j
public class CustomAuthenticationEntryPoint
        implements AuthenticationEntryPoint {

    private static final String exceptionAttributeName = "exceptionCode";

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {

        AuthExceptionCode exceptionCode = (AuthExceptionCode) request.getAttribute(exceptionAttributeName);

        // 사용자 정의 예외의 Enum Code 가 넘어온 경우
        if (exceptionCode != null) {
            setResponse(response, exceptionCode);
        } else {    // Error Message 만 넘어온 경우 범용 인증 예외 코드를 던짐
            log.error("Responding with unauthorized error. Message := {}", authException.getMessage());
            setResponse(response, AUTHENTICATION_ERROR);
        }
    }

    // 에러 응답 생성 메소드
    private void setResponse(
            HttpServletResponse response,
            AuthExceptionCode exceptionCode
    ) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // 한글 출력을 위해 getWriter() 사용
        response.getWriter().print(ExceptionResponse.of(exceptionCode).convertJson());
    }
}
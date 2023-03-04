package site.devtown.spadeworker.domain.auth.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import site.devtown.spadeworker.global.exception.ExceptionResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static site.devtown.spadeworker.domain.auth.exception.AuthExceptionCode.INVALID_USER_ROLE;

@RequiredArgsConstructor
public class TokenAccessDeniedHandler
        implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException {
        setResponse(response);
    }

    // 한글 출력을 위해 getWriter() 사용
    private void setResponse(
            HttpServletResponse response
    ) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().print(ExceptionResponse.of(INVALID_USER_ROLE).convertJson());
    }
}

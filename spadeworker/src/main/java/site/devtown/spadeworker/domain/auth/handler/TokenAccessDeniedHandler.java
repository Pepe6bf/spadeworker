package site.devtown.spadeworker.domain.auth.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class TokenAccessDeniedHandler
        implements AccessDeniedHandler {
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException {
        // TODO : 아래 주석 처리 된 로직 분석하기
//        response.sendError(HttpServletResponse.SC_FORBIDDEN, accessDeniedException.getMessage());
        handlerExceptionResolver.resolveException(request, response, null, accessDeniedException);
    }
}

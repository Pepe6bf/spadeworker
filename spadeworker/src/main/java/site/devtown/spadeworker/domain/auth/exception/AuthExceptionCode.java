package site.devtown.spadeworker.domain.auth.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import site.devtown.spadeworker.global.exception.ExceptionCode;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum AuthExceptionCode
        implements ExceptionCode {
    /**
     * JWT
     * 1 ~ 99
     */
    INVALID_TOKEN(UNAUTHORIZED, "AT-C-001", "유효하지 않은 토큰 입니다."),
    EXPIRED_TOKEN(UNAUTHORIZED, "AT-C-002", "만료된 토큰 입니다."),
    NOT_EXPIRED_TOKEN(BAD_REQUEST, "AT-C-003", "만료되지 않은 토큰 입니다."),
    INVALID_REFRESH_TOKEN(BAD_REQUEST, "AT-C-004", "유효하지 않은 refresh token 입니다."),
    REQUEST_TOKEN_NOT_FOUND(BAD_REQUEST, "AT-C-005", "요청에 토큰이 존재하지 않습니다."),
    INVALID_TOKEN_SIGNATURE(UNAUTHORIZED, "AT-C-006", "유효하지 않은 토큰 시그니쳐입니다."),
    UNSUPPORTED_TOKEN(UNAUTHORIZED, "AT-C-007", "지원하지 않는 토큰입니다."),
    INVALID_TOKEN_COMPACT(BAD_REQUEST, "AT-C-008", "잘못 처리된 토큰 압축입니다."),

    /**
     * OAuth
     * 100 ~ 199
     */
    OAUTH_PROVIDER_MISMATCH(BAD_REQUEST, "AT-C-100", "일치하지 않는 인증 제공자 입니다."),

    /**
     * Common Exception
     * 200 ~
     */
    AUTHENTICATION_ERROR(UNAUTHORIZED, "AT-C-200", "Authentication exception."),
    INTERNAL_AUTHENTICATION_SERVICE_EXCEPTION(INTERNAL_SERVER_ERROR, "AT-S-201", "Internal authentication service exception.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
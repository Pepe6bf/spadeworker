package site.devtown.spadeworker.domain.auth.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import site.devtown.spadeworker.global.exception.ExceptionCode;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum AuthExceptionCode
        implements ExceptionCode {

    DUPLICATED_USER_EMAIL(CONFLICT, "ACC-001", "Duplicated user email"),
    USER_NOT_FOUND(NOT_FOUND, "ACC-002", "User not found"),
    INVALID_TOKEN_SIGNATURE(UNAUTHORIZED, "ACC-003", "Invalid token signature."),
    INVALID_TOKEN(UNAUTHORIZED, "ACC-004", "Invalid token."),
    EXPIRED_TOKEN(UNAUTHORIZED, "ACC-005", "Expired JWT token."),
    UNSUPPORTED_TOKEN(UNAUTHORIZED, "ACC-006", "Unsupported JWT token."),
    INVALID_TOKEN_COMPACT(INTERNAL_SERVER_ERROR, "ACC-007", "JWT token compact of handler are invalid."),
    AUTHENTICATION_ERROR(UNAUTHORIZED, "ACC-008", "Authentication exception."),
    INTERNAL_AUTHENTICATION_SERVICE_EXCEPTION(INTERNAL_SERVER_ERROR, "ACC-009", "Internal authentication service exception.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
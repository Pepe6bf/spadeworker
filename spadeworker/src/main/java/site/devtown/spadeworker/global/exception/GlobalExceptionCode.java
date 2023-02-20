package site.devtown.spadeworker.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum GlobalExceptionCode
        implements ExceptionCode {

    INVALID_INPUT_VALUE(BAD_REQUEST, "C-001", "유효하지 않는 입력 값 입니다."),
    INVALID_REQUEST_METHOD(METHOD_NOT_ALLOWED, "C-002", "유효하지 않는 http 요청 method 입니다."),
    ENTITY_NOT_FOUND(NOT_FOUND, "C-003", "존재하지 않는 리소스 입니다."),
    SERVER_ERROR(INTERNAL_SERVER_ERROR, "S-001", "Internal Server Error");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}

package site.devtown.spadeworker.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GlobalExceptionCode implements ExceptionCode {

    INVALID_INPUT_VALUE(400, "C-001", "유효하지 않는 입력 값 입니다."),
    METHOD_NOT_ALLOWED(405, "C-002", "유효하지 않는 http 요청 method 입니다."),
    ENTITY_NOT_FOUND(404, "C-003", "존재하지 않는 리소스 입니다."),
    INTERNAL_SERVER_ERROR(500, "S-001", "Internal Server Error");

    private final int status;
    private final String code;
    private final String message;
}

package site.devtown.spadeworker.domain.project.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import site.devtown.spadeworker.global.exception.ExceptionCode;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum ProjectExceptionCode
        implements ExceptionCode {

    PROJECT_NOT_FOUND(NOT_FOUND, "PJ-C-001", "존재하지 않는 프로젝트입니다."),
    INVALID_PROJECT_OWNER(FORBIDDEN, "PJ-C-002", "프로젝트를 처리할 권한이 없습니다."),
    PROJECT_DUPLICATE_LIKE(BAD_REQUEST, "PJ-C-003", "프로젝트에 이미 해당 사용자의 좋아요가 존재합니다."),
    PROJECT_LIKE_NOT_FOUND(NOT_FOUND, "PJ-C-004", "프로젝트에 해당 사용자의 좋아요가 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}

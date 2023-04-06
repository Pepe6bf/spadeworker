package site.devtown.spadeworker.domain.file.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import site.devtown.spadeworker.global.exception.ExceptionCode;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum FileExceptionCode
        implements ExceptionCode {

    IMAGE_FILE_NOT_FOUND(NOT_FOUND, "FL-C-001", "존재하지 않는 이미지 파일 입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}

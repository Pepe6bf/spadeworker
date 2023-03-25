package site.devtown.spadeworker.domain.file.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import site.devtown.spadeworker.global.exception.ExceptionResponse;

import static site.devtown.spadeworker.domain.file.exception.FileExceptionCode.IMAGE_FILE_NOT_FOUND;

@Slf4j
@RestControllerAdvice
public class FileExceptionHandler {

    /**
     * ImageFileNotFoundException 핸들링
     */
    @ExceptionHandler(ImageFileNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleImageFileNotFoundException(
            ImageFileNotFoundException e
    ) {
        log.error("{}", e.getMessage());
        return new ResponseEntity<>(
                ExceptionResponse.of(IMAGE_FILE_NOT_FOUND, IMAGE_FILE_NOT_FOUND.getMessage()),
                HttpStatus.valueOf(IMAGE_FILE_NOT_FOUND.getHttpStatus().value())
        );
    }

}
package site.devtown.spadeworker.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

import static site.devtown.spadeworker.global.exception.GlobalExceptionCode.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Validation 예외 핸들링
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e
    ) {
        log.error("handle MethodArgumentNotValidException");
        return new ResponseEntity<>(
                ExceptionResponse.of(INVALID_INPUT_VALUE, e.getBindingResult()),
                HttpStatus.valueOf(INVALID_INPUT_VALUE.getHttpStatus().value())
        );
    }

    /**
     * EntityNotFound 예외 핸들링
     */
    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<ExceptionResponse> handleEntityNotFoundException(
            EntityNotFoundException e
    ) {
        log.error("handle EntityNotFoundException");
        return new ResponseEntity<>(
                ExceptionResponse.of(ENTITY_NOT_FOUND, e.getMessage()),
                HttpStatus.valueOf(ENTITY_NOT_FOUND.getHttpStatus().value())
        );
    }

    /**
     * 유효하지 않은 클라이언트의 요청 값 예외 핸들링
     */
    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ExceptionResponse> handleIllegalArgumentException(
            IllegalArgumentException e
    ) {
        log.error("handle IllegalArgumentException");
        return new ResponseEntity<>(
                ExceptionResponse.of(INVALID_INPUT_VALUE, e.getMessage()),
                HttpStatus.valueOf(INVALID_INPUT_VALUE.getHttpStatus().value())
        );
    }

    /**
     * 유효하지 않은 HTTP Method 요청 예외 핸들링
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ExceptionResponse> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e
    ) {
        log.error("handle HttpRequestMethodNotSupportedException");
        return new ResponseEntity<>(
                ExceptionResponse.of(INVALID_REQUEST_METHOD),
                HttpStatus.valueOf(INVALID_REQUEST_METHOD.getHttpStatus().value())
        );
    }

    /**
     * 유효하지 않은 타입 변환 예외 핸들링
     */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ExceptionResponse> handleBindException(
            BindException e
    ) {
        log.error("handle BindException");
        return new ResponseEntity<>(
                ExceptionResponse.of(INVALID_INPUT_VALUE, e.getBindingResult()),
                HttpStatus.valueOf(INVALID_INPUT_VALUE.getHttpStatus().value())
        );
    }

    /**
     * 최상위 예외 핸들링
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ExceptionResponse> handleException(
            Exception e
    ) {
        log.error("handle Exception", e);
        return new ResponseEntity<>(
                ExceptionResponse.of(SERVER_ERROR),
                HttpStatus.valueOf(SERVER_ERROR.getHttpStatus().value())
        );
    }
}

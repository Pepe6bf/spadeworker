package site.devtown.spadeworker.domain.auth.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import site.devtown.spadeworker.global.exception.ExceptionResponse;

import static site.devtown.spadeworker.domain.auth.exception.AuthExceptionCode.*;

@Slf4j
@RestControllerAdvice
public class AuthExceptionHandler {

    /**
     * SecurityException 예외 핸들링
     */
    @ExceptionHandler(SecurityException.class)
    protected ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(
            SecurityException e
    ) {
        log.error("handle SecurityException");
        return new ResponseEntity<>(
                ExceptionResponse.of(INVALID_TOKEN_SIGNATURE, INVALID_TOKEN_SIGNATURE.getMessage()),
                HttpStatus.valueOf(INVALID_TOKEN_SIGNATURE.getHttpStatus().value())
        );
    }

    /**
     * MalformedJwtException 예외 핸들링
     */
    @ExceptionHandler(MalformedJwtException.class)
    protected ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(
            MalformedJwtException e
    ) {
        log.error("handle MalformedJwtException");
        return new ResponseEntity<>(
                ExceptionResponse.of(INVALID_TOKEN, INVALID_TOKEN.getMessage()),
                HttpStatus.valueOf(INVALID_TOKEN.getHttpStatus().value())
        );
    }

    /**
     * ExpiredJwtException 예외 핸들링
     */
    @ExceptionHandler(ExpiredJwtException.class)
    protected ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(
            ExpiredJwtException e
    ) {
        log.error("handle ExpiredJwtException");
        return new ResponseEntity<>(
                ExceptionResponse.of(EXPIRED_TOKEN, EXPIRED_TOKEN.getMessage()),
                HttpStatus.valueOf(EXPIRED_TOKEN.getHttpStatus().value())
        );
    }

    /**
     * UnsupportedJwtException 예외 핸들링
     */
    @ExceptionHandler(UnsupportedJwtException.class)
    protected ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(
            UnsupportedJwtException e
    ) {
        log.error("handle UnsupportedJwtException");
        return new ResponseEntity<>(
                ExceptionResponse.of(UNSUPPORTED_TOKEN, UNSUPPORTED_TOKEN.getMessage()),
                HttpStatus.valueOf(UNSUPPORTED_TOKEN.getHttpStatus().value())
        );
    }

    /**
     * AuthenticationException 예외 핸들링
     */
    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(
            AuthenticationException e
    ) {
        log.error("handle AuthenticationException");
        return new ResponseEntity<>(
                ExceptionResponse.of(AUTHENTICATION_ERROR, AUTHENTICATION_ERROR.getMessage()),
                HttpStatus.valueOf(AUTHENTICATION_ERROR.getHttpStatus().value())
        );
    }

    /**
     * InternalAuthenticationServiceException 예외 핸들링
     */
    @ExceptionHandler(InternalAuthenticationServiceException.class)
    protected ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(
            InternalAuthenticationServiceException e
    ) {
        log.error("handle InternalAuthenticationServiceException");
        return new ResponseEntity<>(
                ExceptionResponse.of(INTERNAL_AUTHENTICATION_SERVICE_EXCEPTION, INTERNAL_AUTHENTICATION_SERVICE_EXCEPTION.getMessage()),
                HttpStatus.valueOf(INTERNAL_AUTHENTICATION_SERVICE_EXCEPTION.getHttpStatus().value())
        );
    }

    /**
     * IllegalArgumentException 예외 핸들링
     */
    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(
            IllegalArgumentException e
    ) {
        log.error("handle IllegalArgumentException");
        return new ResponseEntity<>(
                ExceptionResponse.of(INVALID_TOKEN_COMPACT, INVALID_TOKEN_COMPACT.getMessage()),
                HttpStatus.valueOf(INVALID_TOKEN_COMPACT.getHttpStatus().value())
        );
    }
}

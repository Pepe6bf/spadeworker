package site.devtown.spadeworker.domain.auth.exception;

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
     * TokenValidFailedException 핸들링
     * Custom Exception
     */
    @ExceptionHandler(InvalidTokenException.class)
    protected ResponseEntity<ExceptionResponse> handleInvalidTokenException(
            InvalidTokenException e
    ) {
        AuthExceptionCode authExceptionCode = e.getAuthExceptionCode();
        log.error("{}", e.getMessage());
        return new ResponseEntity<>(
                ExceptionResponse.of(authExceptionCode, authExceptionCode.getMessage()),
                HttpStatus.valueOf(authExceptionCode.getHttpStatus().value())
        );
    }

    /**
     * NotExpiredTokenException 핸들링
     * Custom Exception
     */
    @ExceptionHandler(NotExpiredTokenException.class)
    protected ResponseEntity<ExceptionResponse> handleNotExpiredTokenException(
            NotExpiredTokenException e
    ) {
        log.error("handle NotExpiredTokenException");
        return new ResponseEntity<>(
                ExceptionResponse.of(NOT_EXPIRED_TOKEN, NOT_EXPIRED_TOKEN.getMessage()),
                HttpStatus.valueOf(NOT_EXPIRED_TOKEN.getHttpStatus().value())
        );
    }

    /**
     * OAuthProviderMissMatchException 핸들링
     * Custom Exception
     */
    @ExceptionHandler(OAuthProviderMisMatchException.class)
    protected ResponseEntity<ExceptionResponse> handleOAuthProviderMissMatchException(
            OAuthProviderMisMatchException e
    ) {
        log.error("handle OAuthProviderMissMatchException");
        return new ResponseEntity<>(
                ExceptionResponse.of(OAUTH_PROVIDER_MISMATCH, e.getMessage()),
                HttpStatus.valueOf(OAUTH_PROVIDER_MISMATCH.getHttpStatus().value())
        );
    }

    /**
     * AuthenticationException 핸들링
     * Built-in Exception
     */
    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<ExceptionResponse> handleAuthenticationException(
            AuthenticationException e
    ) {
        log.error("handle AuthenticationException");
        return new ResponseEntity<>(
                ExceptionResponse.of(AUTHENTICATION_ERROR, AUTHENTICATION_ERROR.getMessage()),
                HttpStatus.valueOf(AUTHENTICATION_ERROR.getHttpStatus().value())
        );
    }

    /**
     * InternalAuthenticationServiceException 핸들링
     * Built-in Exception
     */
    @ExceptionHandler(InternalAuthenticationServiceException.class)
    protected ResponseEntity<ExceptionResponse> handleInternalAuthenticationServiceException(
            InternalAuthenticationServiceException e
    ) {
        log.error("handle InternalAuthenticationServiceException");
        return new ResponseEntity<>(
                ExceptionResponse.of(INTERNAL_AUTHENTICATION_SERVICE_EXCEPTION, INTERNAL_AUTHENTICATION_SERVICE_EXCEPTION.getMessage()),
                HttpStatus.valueOf(INTERNAL_AUTHENTICATION_SERVICE_EXCEPTION.getHttpStatus().value())
        );
    }
}

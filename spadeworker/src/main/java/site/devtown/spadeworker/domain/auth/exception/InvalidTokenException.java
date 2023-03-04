package site.devtown.spadeworker.domain.auth.exception;

import io.jsonwebtoken.Claims;

public class InvalidTokenException extends RuntimeException {

    private final AuthExceptionCode authExceptionCode;
    private Claims expiredTokenClaims;

    public InvalidTokenException(AuthExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.authExceptionCode = exceptionCode;
    }

    public InvalidTokenException(
            AuthExceptionCode exceptionCode,
            Claims expiredTokenClaims
    ) {
        super(exceptionCode.getMessage());
        this.authExceptionCode = exceptionCode;
        this.expiredTokenClaims = expiredTokenClaims;
    }

    public AuthExceptionCode getAuthExceptionCode() {
        return authExceptionCode;
    }

    public Claims getExpiredTokenClaims() {
        return expiredTokenClaims;
    }
}

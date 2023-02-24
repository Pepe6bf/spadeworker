package site.devtown.spadeworker.domain.auth.exception;

public class NotExpiredTokenException extends RuntimeException {

    public NotExpiredTokenException() {
        super("Not Expired Token.");
    }

    public NotExpiredTokenException(String message) {
        super(message);
    }
}

package site.devtown.spadeworker.domain.auth.exception;

public class ExpiredTokenException extends RuntimeException {

    public ExpiredTokenException() {
        super("Expired Token.");
    }

    public ExpiredTokenException(String message) {
        super(message);
    }
}

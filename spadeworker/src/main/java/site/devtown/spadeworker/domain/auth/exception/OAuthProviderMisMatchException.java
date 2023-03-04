package site.devtown.spadeworker.domain.auth.exception;

public class OAuthProviderMisMatchException extends RuntimeException {

    public OAuthProviderMisMatchException(String message) {
        super(message);
    }
}

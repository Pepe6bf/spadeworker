package site.devtown.spadeworker.domain.auth.exception;

import static site.devtown.spadeworker.domain.auth.exception.AuthExceptionCode.LOGGED_USER_NOT_FOUND;

public class LoggedUserNotFoundException extends RuntimeException {
    public LoggedUserNotFoundException() {
        super(LOGGED_USER_NOT_FOUND.getMessage());
    }
}

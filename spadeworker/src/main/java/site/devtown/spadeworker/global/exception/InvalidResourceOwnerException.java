package site.devtown.spadeworker.global.exception;

public class InvalidResourceOwnerException
        extends RuntimeException {
    private final ExceptionCode code;

    public InvalidResourceOwnerException(
            ExceptionCode exceptionCode
    ) {
        super(exceptionCode.getMessage());
        this.code = exceptionCode;
    }

    public ExceptionCode getCode() {
        return code;
    }
}

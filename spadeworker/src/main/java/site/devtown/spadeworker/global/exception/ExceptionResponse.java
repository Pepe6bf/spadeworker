package site.devtown.spadeworker.global.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
import site.devtown.spadeworker.global.util.GsonUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExceptionResponse {

    private Integer status;
    private String code;
    private String message;
    private List<FieldError> errors;
    private LocalDateTime timestamp;

    private ExceptionResponse(
            final ExceptionCode exceptionCode
    ) {
        this.message = exceptionCode.getMessage();
        this.status = exceptionCode.getHttpStatus().value();
        this.code = exceptionCode.getCode();
        this.timestamp = LocalDateTime.now();
        this.errors = new ArrayList<>();
    }

    private ExceptionResponse(
            final ExceptionCode exceptionCode,
            final String message
    ) {
        this.message = message;
        this.status = exceptionCode.getHttpStatus().value();
        this.code = exceptionCode.getCode();
        this.timestamp = LocalDateTime.now();
        this.errors = new ArrayList<>();
    }

    private ExceptionResponse(
            final ExceptionCode exceptionCode,
            final List<FieldError> errors
    ) {
        this.message = exceptionCode.getMessage();
        this.status = exceptionCode.getHttpStatus().value();
        this.code = exceptionCode.getCode();
        this.timestamp = LocalDateTime.now();
        this.errors = errors;
    }

    public static ExceptionResponse of(
            final ExceptionCode exceptionCode
    ) {
        return new ExceptionResponse(exceptionCode);
    }

    public static ExceptionResponse of(
            final ExceptionCode exceptionCode,
            final String message
    ) {
        return new ExceptionResponse(exceptionCode, message);
    }

    public static ExceptionResponse of(
            final ExceptionCode code,
            final BindingResult bindingResult
    ) {
        return new ExceptionResponse(code, FieldError.of(bindingResult));
    }

    public static ExceptionResponse of(
            final ExceptionCode exceptionCode,
            final List<FieldError> errors
    ) {
        return new ExceptionResponse(exceptionCode, errors);
    }

    /**
     * Object -> Json (필터에서 사용)
     */
    public String convertJson() {
        return new GsonUtil().toJson(this);
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FieldError {
        private String field;
        private String value;
        private String reason;

        private FieldError(
                String field,
                String value,
                String reason
        ) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        public static List<FieldError> of(
                final String field,
                final String value,
                final String reason
        ) {
            List<FieldError> fieldErrors = new ArrayList<>();
            fieldErrors.add(new FieldError(field, value, reason));

            return fieldErrors;
        }

        private static List<FieldError> of(
                final BindingResult bindingResult
        ) {
            final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();

            return fieldErrors.stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()))
                    .collect(Collectors.toList());
        }
    }
}
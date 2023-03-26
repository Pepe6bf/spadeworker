package site.devtown.spadeworker.domain.file.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Constraint(validatedBy = ContentImageValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ContentImageValidate {

    String message() default "유효하지 않은 이미지 리소스입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
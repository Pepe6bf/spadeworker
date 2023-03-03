package site.devtown.spadeworker.domain.project.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Constraint(validatedBy = ProjectThumbnailImageValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ProjectThumbnailImageValidate {

    String message() default "유효하지 않은 이미지 리소스입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

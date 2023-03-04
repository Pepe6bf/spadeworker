package site.devtown.spadeworker.domain.project.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Constraint(validatedBy = ProjectTitleValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ProjectTitleDuplicateValidate {

    String message() default "이미 사용중인 프로젝트 제목입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

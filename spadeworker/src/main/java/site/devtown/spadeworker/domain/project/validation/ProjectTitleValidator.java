package site.devtown.spadeworker.domain.project.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.devtown.spadeworker.domain.project.repository.ProjectRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
@Component
public class ProjectTitleValidator
        implements ConstraintValidator<ProjectTitleDuplicateValidate, String> {

    private final ProjectRepository projectRepository;

    @Override
    public boolean isValid(
            String title,
            ConstraintValidatorContext context
    ) {
        boolean isValid = !projectRepository.existsByTitle(title);

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("이미 사용중인 프로젝트 제목입니다.")
                    .addConstraintViolation();
        }

        return isValid;
    }

    @Override
    public void initialize(ProjectTitleDuplicateValidate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}

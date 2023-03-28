package site.devtown.spadeworker.domain.file.validation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import site.devtown.spadeworker.global.util.ImageUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

@Component
public class ThumbnailImageValidator
        implements ConstraintValidator<ThumbnailImageValidate, MultipartFile> {

    @Value("${image.allow-extensions}")
    private List<String> allowImageExtensions;

    @Value("${image.project-thumbnail-image.limit-size}")
    private long limitImageSize;

    @Override
    public boolean isValid(
            MultipartFile uploadImage,
            ConstraintValidatorContext context
    ) {

        // 프로필 썸네일이 Null 인지 체크
        if (uploadImage == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("프로필 썸네일은 필수 값 입니다.")
                    .addConstraintViolation();
            return false;
        }

        // 입력 값으로 넘어온 이미지가 빈 값일 경우 기본 이미지를 사용한다고 판단
        if (uploadImage.isEmpty()) {
            return true;
        }

        // 이미지 확장자 검증
        if (!validateImageExtension(ImageUtil.getImageExtension(uploadImage.getOriginalFilename()))) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("유효하지 않은 이미지 확장자 입니다.")
                    .addConstraintViolation();
            return false;
        }

        // 이미지 크기 검증
        if (!validateImageSize(uploadImage.getSize())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("유효하지 않은 이미지 크기 입니다.")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }

    @Override
    public void initialize(ThumbnailImageValidate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    // 요청된 이미지의 확장자가 화이트 리스트에 포함되는지 검증
    private boolean validateImageExtension(String imageExtension) {
        return allowImageExtensions.contains(imageExtension);
    }

    // 요청된 이미지의 크기가 유효한 크기인지 검증
    private boolean validateImageSize(long imageSize) {
        return imageSize <= limitImageSize;
    }
}

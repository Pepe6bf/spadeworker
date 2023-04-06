package site.devtown.spadeworker.domain.file.validation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import site.devtown.spadeworker.global.util.ImageUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class ThumbnailImageValidator
        implements ConstraintValidator<ThumbnailImageValidate, MultipartFile> {

    @Value("${image.allow-extensions}")
    private List<String> allowImageExtensions;

    @Value("${image.limit-size}")
    private long limitImageSize;

    @Override
    public boolean isValid(
            MultipartFile requestImage,
            ConstraintValidatorContext context
    ) {

        // 요청된 이미지 값이 Null 인지 체크
        if (requestImage == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("썸네일 이미지 값은 필수 입니다.")
                    .addConstraintViolation();
            return false;
        }

        // 입력 값으로 넘어온 이미지가 빈 값일 경우 기본 이미지를 사용한다고 판단
        if (requestImage.isEmpty()) {
            return true;
        }

        // 이미지 확장자 검증
        if (!validateImageExtension(ImageUtil.getImageExtension(requestImage.getOriginalFilename()))) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("유효하지 않은 이미지 확장자 입니다.")
                    .addConstraintViolation();
            return false;
        }

        // 이미지 크기 검증
        if (!validateImageSize(requestImage.getSize())) {
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

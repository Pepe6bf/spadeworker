package site.devtown.spadeworker.domain.project.dto;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;
import site.devtown.spadeworker.domain.project.validation.ProjectThumbnailImageValidate;
import site.devtown.spadeworker.domain.project.validation.ProjectTitleDuplicateValidate;

import javax.validation.constraints.NotBlank;

public record UpdateProjectRequest(
        @NotBlank(message = "프로젝트 제목은 필수 값입니다.")
        @Length(min = 1, max = 20, message = "프로젝트 제목 길이 제한은 1이상 20이하 입니다.")
        @ProjectTitleDuplicateValidate
        String title,

        @NotBlank(message = "프로젝트 설명은 필수 값입니다.")
        @Length(min = 1, max = 500, message = "프로젝트 설명 길이 제한은 1이상 500이하 입니다.")
        String description,

        @ThumbnailImageValidate
        MultipartFile thumbnailImage
) {
}

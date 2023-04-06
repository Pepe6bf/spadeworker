package site.devtown.spadeworker.domain.file.dto;

import org.springframework.web.multipart.MultipartFile;
import site.devtown.spadeworker.domain.file.validation.ContentImageValidate;

public record UploadSingleImageRequest(
        @ContentImageValidate
        MultipartFile image
) {}

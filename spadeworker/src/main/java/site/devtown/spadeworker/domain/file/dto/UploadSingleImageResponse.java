package site.devtown.spadeworker.domain.file.dto;

public record UploadSingleImageResponse(
        String originalFileName,
        String storedImageFullPath
) {

    public static UploadSingleImageResponse of(
            String originalFileName,
            String storedImageFullPath
    ) {
        return new UploadSingleImageResponse(originalFileName, storedImageFullPath);
    }
}

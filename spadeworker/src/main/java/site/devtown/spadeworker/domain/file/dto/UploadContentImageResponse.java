package site.devtown.spadeworker.domain.file.dto;

public record UploadContentImageResponse(
        String originalFileName,
        String storedImageFullPath
) {

    public static UploadContentImageResponse of(
            String originalFileName,
            String storedImageFullPath
    ) {
        return new UploadContentImageResponse(originalFileName, storedImageFullPath);
    }
}

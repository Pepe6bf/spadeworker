package site.devtown.spadeworker.domain.article.dto;

public record SaveTempArticleResponse(
        Long tempArticleId
) {
    public static SaveTempArticleResponse of(
            Long tempArticleId
    ) {
        return new SaveTempArticleResponse(tempArticleId);
    }
}
package site.devtown.spadeworker.domain.article.dto;

public record ArticleIdResponse(
        Long articleId
) {
    public static ArticleIdResponse of(
            Long articleId
    ) {
        return new ArticleIdResponse(articleId);
    }
}
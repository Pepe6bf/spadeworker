package site.devtown.spadeworker.domain.article.dto;

public record CreateArticleResponse(
        Long articleId
) {
    public static CreateArticleResponse of(
            Long articleId
    ) {
        return new CreateArticleResponse(articleId);
    }
}
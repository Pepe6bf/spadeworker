package site.devtown.spadeworker.domain.article.dto;

import site.devtown.spadeworker.domain.article.model.entity.Article;

import java.util.Set;

public record TempArticleDto(
        String title,
        String content,
        Set<String> hashtags
) {

    public static TempArticleDto from(
            Article article,
            Set<String> hashtags
    ) {
        return new TempArticleDto(
                article.getTitle(),
                article.getContent(),
                hashtags
        );
    }
}
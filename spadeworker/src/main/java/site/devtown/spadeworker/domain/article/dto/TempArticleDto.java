package site.devtown.spadeworker.domain.article.dto;

import site.devtown.spadeworker.domain.article.model.entity.TempArticle;

import java.util.Set;

public record TempArticleDto(
        String title,
        String content,
        Set<String> hashtags
) {

    public static TempArticleDto from(
            TempArticle tempArticle,
            Set<String> hashtags
    ) {
        return new TempArticleDto(
                tempArticle.getTitle(),
                tempArticle.getContent(),
                hashtags
        );
    }
}
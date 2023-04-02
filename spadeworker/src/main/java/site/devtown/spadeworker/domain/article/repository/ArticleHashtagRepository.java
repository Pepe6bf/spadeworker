package site.devtown.spadeworker.domain.article.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.devtown.spadeworker.domain.article.model.entity.Article;
import site.devtown.spadeworker.domain.article.model.entity.ArticleHashtag;

import java.util.Set;

public interface ArticleHashtagRepository
        extends JpaRepository<ArticleHashtag, Long> {

    void deleteAllByArticle(Article article);

    Set<ArticleHashtag> findAllByArticle(Article article);
}
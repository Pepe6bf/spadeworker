package site.devtown.spadeworker.domain.article.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.devtown.spadeworker.domain.article.model.entity.Article;
import site.devtown.spadeworker.domain.article.model.entity.ArticleContentImage;

import java.util.List;

public interface ArticleContentImageRepository
        extends JpaRepository<ArticleContentImage, Long> {

    List<ArticleContentImage> findAllByArticle(Article article);
}
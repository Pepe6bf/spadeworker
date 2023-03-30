package site.devtown.spadeworker.domain.article.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.devtown.spadeworker.domain.article.model.entity.ArticleContentImage;

public interface ArticleContentImageRepository
        extends JpaRepository<ArticleContentImage, Long> {
}
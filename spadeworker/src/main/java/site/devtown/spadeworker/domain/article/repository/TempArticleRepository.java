package site.devtown.spadeworker.domain.article.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.devtown.spadeworker.domain.article.model.entity.TempArticle;

public interface TempArticleRepository
        extends JpaRepository<TempArticle, Long> {
}

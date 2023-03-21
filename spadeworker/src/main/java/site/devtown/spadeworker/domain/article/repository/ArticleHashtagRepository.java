package site.devtown.spadeworker.domain.article.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.devtown.spadeworker.domain.article.model.entity.ArticleHashtag;

public interface ArticleHashtagRepository
        extends JpaRepository<ArticleHashtag, Long> {
}

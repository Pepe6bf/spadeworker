package site.devtown.spadeworker.domain.article.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.devtown.spadeworker.domain.article.model.entity.TempArticle;
import site.devtown.spadeworker.domain.article.model.entity.TempArticleHashtag;

public interface TempArticleHashtagRepository
        extends JpaRepository<TempArticleHashtag, Long> {

    void deleteAllByTempArticle(TempArticle tempArticle);
}

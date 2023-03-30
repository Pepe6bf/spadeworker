package site.devtown.spadeworker.domain.article.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.devtown.spadeworker.domain.article.model.entity.TempArticle;
import site.devtown.spadeworker.domain.article.model.entity.TempArticleHashtag;

import java.util.Set;

public interface TempArticleHashtagRepository
        extends JpaRepository<TempArticleHashtag, Long> {

    void deleteAllByTempArticle(TempArticle tempArticle);

    Set<TempArticleHashtag> findAllByTempArticle(TempArticle tempArticle);
}
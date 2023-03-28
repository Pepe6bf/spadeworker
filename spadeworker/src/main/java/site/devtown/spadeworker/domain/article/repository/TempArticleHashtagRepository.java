package site.devtown.spadeworker.domain.article.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.devtown.spadeworker.domain.article.model.entity.TempArticle;
import site.devtown.spadeworker.domain.article.model.entity.TempArticleHashtag;

import java.util.List;
import java.util.Set;

public interface TempArticleHashtagRepository
        extends JpaRepository<TempArticleHashtag, Long> {

    void deleteAllByTempArticle(TempArticle tempArticle);
    List<TempArticleHashtag> findAllByTempArticle(TempArticle tempArticle);
}
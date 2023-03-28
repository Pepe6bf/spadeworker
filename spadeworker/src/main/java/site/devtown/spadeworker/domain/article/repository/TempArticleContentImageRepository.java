package site.devtown.spadeworker.domain.article.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.devtown.spadeworker.domain.article.model.entity.TempArticle;
import site.devtown.spadeworker.domain.article.model.entity.TempArticleContentImage;

import java.util.List;

public interface TempArticleContentImageRepository
        extends JpaRepository<TempArticleContentImage, Long> {

    List<TempArticleContentImage> findAllByTempArticle(TempArticle tempArticle);
}
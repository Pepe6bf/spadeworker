package site.devtown.spadeworker.domain.article.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.devtown.spadeworker.domain.article.model.entity.TempArticle;
import site.devtown.spadeworker.domain.article.model.entity.TempArticleThumbnailImage;

import java.util.Optional;

public interface TempArticleThumbnailImageRepository
        extends JpaRepository<TempArticleThumbnailImage, Long> {

    Optional<TempArticleThumbnailImage> findByTempArticle(TempArticle tempArticle);

    void deleteAllByTempArticle(TempArticle tempArticle);

    boolean existsByTempArticle(TempArticle tempArticle);
}
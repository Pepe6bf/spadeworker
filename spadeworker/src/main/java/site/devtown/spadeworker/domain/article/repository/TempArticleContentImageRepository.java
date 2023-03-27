package site.devtown.spadeworker.domain.article.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.devtown.spadeworker.domain.article.model.entity.TempArticleContentImage;

public interface TempArticleContentImageRepository
        extends JpaRepository<TempArticleContentImage, Long> {
}

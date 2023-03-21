package site.devtown.spadeworker.domain.article.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.devtown.spadeworker.domain.article.model.entity.Hashtag;

public interface HashtagRepository extends
        JpaRepository<Hashtag, Long> {
}

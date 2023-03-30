package site.devtown.spadeworker.domain.article.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.devtown.spadeworker.domain.article.model.entity.Hashtag;

import java.util.Optional;

public interface HashtagRepository extends
        JpaRepository<Hashtag, Long> {

    boolean existsByTitle(String title);

    Optional<Hashtag> findByTitle(String title);
}

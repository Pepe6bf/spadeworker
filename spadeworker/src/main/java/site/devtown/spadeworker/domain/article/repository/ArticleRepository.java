package site.devtown.spadeworker.domain.article.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.devtown.spadeworker.domain.article.model.constant.ArticleStatus;
import site.devtown.spadeworker.domain.article.model.entity.Article;
import site.devtown.spadeworker.domain.user.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository
        extends JpaRepository<Article, Long> {

    Optional<Article> findByIdAndStatus(Long id, ArticleStatus status);

    List<Article> findByUserAndStatus(User user, ArticleStatus status);
}

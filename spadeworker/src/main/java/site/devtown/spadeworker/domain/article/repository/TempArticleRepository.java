package site.devtown.spadeworker.domain.article.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.devtown.spadeworker.domain.article.model.entity.TempArticle;
import site.devtown.spadeworker.domain.user.model.entity.User;

import java.util.List;

public interface TempArticleRepository
        extends JpaRepository<TempArticle, Long> {

    List<TempArticle> findAllByUser(User user);
}
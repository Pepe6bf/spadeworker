package site.devtown.spadeworker.domain.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.devtown.spadeworker.domain.project.entity.Project;
import site.devtown.spadeworker.domain.project.entity.ProjectLike;
import site.devtown.spadeworker.domain.user.model.entity.User;

import java.util.Optional;

public interface ProjectLikeRepository
        extends JpaRepository<ProjectLike, Long> {

    Boolean existsByProjectAndUser(Project project, User user);

    Optional<ProjectLike> findByProjectAndUser(Project project, User user);

    int countAllByProject(Project project);
}

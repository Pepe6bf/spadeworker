package site.devtown.spadeworker.domain.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.devtown.spadeworker.domain.project.entity.Project;
import site.devtown.spadeworker.domain.project.entity.ProjectSubscribe;
import site.devtown.spadeworker.domain.user.model.entity.User;

import java.util.Optional;

public interface ProjectSubscribeRepository
        extends JpaRepository<ProjectSubscribe, Long> {

    Boolean existsByProjectAndSubscriber(Project project, User subscriber);

    Optional<ProjectSubscribe> findByProjectAndSubscriber(Project project, User subscriber);
}

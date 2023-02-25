package site.devtown.spadeworker.domain.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.devtown.spadeworker.domain.project.entity.ProjectSubscribe;

public interface ProjectSubscribeRepository
        extends JpaRepository<ProjectSubscribe, Long> {
}

package site.devtown.spadeworker.domain.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.devtown.spadeworker.domain.project.entity.Project;

public interface ProjectRepository
        extends JpaRepository<Project, Long> {

    boolean existsByTitle(String title);
}

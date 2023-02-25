package site.devtown.spadeworker.domain.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.devtown.spadeworker.domain.project.entity.ProjectLike;

public interface ProjectLikeRepository
        extends JpaRepository<ProjectLike, Long> {
}

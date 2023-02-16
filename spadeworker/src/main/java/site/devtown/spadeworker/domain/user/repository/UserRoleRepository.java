package site.devtown.spadeworker.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.devtown.spadeworker.domain.user.model.entity.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
}

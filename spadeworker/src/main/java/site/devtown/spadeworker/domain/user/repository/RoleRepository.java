package site.devtown.spadeworker.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.devtown.spadeworker.domain.user.model.constant.UserRoleType;
import site.devtown.spadeworker.domain.user.model.entity.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleType(UserRoleType roleType);
}

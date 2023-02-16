package site.devtown.spadeworker.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.devtown.spadeworker.domain.user.model.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}

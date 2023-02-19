package site.devtown.spadeworker.domain.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.devtown.spadeworker.domain.auth.token.UserRefreshToken;

import java.util.Optional;

public interface UserRefreshTokenRepository
        extends JpaRepository<UserRefreshToken, Long> {
}

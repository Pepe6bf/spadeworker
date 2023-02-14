package site.devtown.spadeworker.global.config.audit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class AuditConfig {
    @Bean
    public AuditorAware<String> auditorProvider() {
        // TODO: 인증 기능 도입 후 사용자의 personalId 로 자동 설정하도록 리펙토링
        return () -> Optional.of("tester");
    }
}
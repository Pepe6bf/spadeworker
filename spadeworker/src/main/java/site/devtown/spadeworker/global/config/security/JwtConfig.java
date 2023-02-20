package site.devtown.spadeworker.global.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import site.devtown.spadeworker.domain.auth.filter.TokenAuthenticationFilter;
import site.devtown.spadeworker.domain.auth.token.AuthTokenProvider;
import site.devtown.spadeworker.global.factory.YamlPropertySourceFactory;

@PropertySource(
        value = "classpath:/jwt.yml",
        factory = YamlPropertySourceFactory.class
)
@Configuration
public class JwtConfig {

    @Value("${jwt.secret.access-token-secret-key}")
    private String accessTokenSecretKey;
    @Value("${jwt.expiry.access-token-expiry}")
    private long accessTokenExpiry;
    @Value("${jwt.secret.refresh-token-secret-key}")
    private String refreshTokenSecretKey;
    @Value("${jwt.expiry.refresh-token-expiry}")
    private long refreshTokenExpiry;

    /**
     * Token Provider 설정
     */
    @Bean
    public AuthTokenProvider jwtProvider() {
        return new AuthTokenProvider(
                accessTokenSecretKey,
                accessTokenExpiry,
                refreshTokenSecretKey,
                refreshTokenExpiry
        );
    }

    /*
     * 토큰 검증 필터 설정
     * */
    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(jwtProvider());
    }
}
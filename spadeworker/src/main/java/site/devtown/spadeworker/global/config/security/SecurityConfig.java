package site.devtown.spadeworker.global.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;
import site.devtown.spadeworker.domain.auth.exception.CustomAuthenticationEntryPoint;
import site.devtown.spadeworker.domain.auth.filter.TokenAuthenticationFilter;
import site.devtown.spadeworker.domain.auth.handler.OAuth2AuthenticationFailureHandler;
import site.devtown.spadeworker.domain.auth.handler.OAuth2AuthenticationSuccessHandler;
import site.devtown.spadeworker.domain.auth.handler.TokenAccessDeniedHandler;
import site.devtown.spadeworker.domain.auth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import site.devtown.spadeworker.domain.auth.service.JwtService;

import static org.springframework.http.HttpMethod.GET;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final TokenAccessDeniedHandler tokenAccessDeniedHandler;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository;
    private final JwtService jwtService;
    private final OAuth2UserService oAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        // CSRF & Form Login 기능 비활성화
        http
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable();

        // 세션을 사용하지 않기 때문에, 세션 설정을 STATELESS 로 실행
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 권한별 접근 요청 설정
        http
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                // AUTH API
                .antMatchers(GET, "/api/auth/refresh").permitAll()
                // Project API
                .antMatchers(GET, "/api/projects/**").permitAll()
                // 나머지는 모두 인증 필요
                .anyRequest().authenticated();

        // JWT 검증 필터 및 예외처리 등록
        http
                .addFilterBefore(
                        new TokenAuthenticationFilter(jwtService),
                        UsernamePasswordAuthenticationFilter.class
                )
                .exceptionHandling()
                // 토큰 인증 필터에서 발생한 예외를 처리
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .accessDeniedHandler(tokenAccessDeniedHandler);

        // front 에서 login 시 요청할 url
        http.oauth2Login()
                .authorizationEndpoint()
                .baseUri("/api/oauth2/authorization")
                .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository);

        // OAuth Server 리다이렉션 주소
        http.oauth2Login()
                .redirectionEndpoint()
                .baseUri("/*/oauth2/code/*");

        // 인증 후 user 정보를 파싱할 서비스 등록
        http.oauth2Login()
                .userInfoEndpoint()
                .userService(oAuth2UserService);

        // OAuth2 성공/실패 시 처리 할 핸들러 등록
        http.oauth2Login()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler);

        return http.build();
    }

    /**
     * Password Encoder
     */
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

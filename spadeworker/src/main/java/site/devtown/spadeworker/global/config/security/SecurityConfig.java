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

import static org.springframework.http.HttpMethod.GET;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final TokenAccessDeniedHandler tokenAccessDeniedHandler;
    private final TokenAuthenticationFilter tokenAuthenticationFilter;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository;
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

        // 토큰 예외 처리 설정
        http
                .exceptionHandling()
                // 토큰 인증 필터에서 발생한 예외를 처리
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                // 401, 403 예외 핸들러를 직접 제작한 핸들러로 넣어줌
                .accessDeniedHandler(tokenAccessDeniedHandler);

        // 권한별 접근 요청 설정
        http
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers(GET, "/api/auth/refresh").permitAll()
                // 나머지는 모두 인증 필요
                .anyRequest().authenticated();

        // 인증 된 사용자 토큰 검증 필터 설정
        http
                .addFilterBefore(
                        tokenAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        // front 에서 login 시 요청할 url
        http.oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorization")
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

package site.devtown.spadeworker.domain.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import site.devtown.spadeworker.domain.auth.exception.OAuthProviderMissMatchException;
import site.devtown.spadeworker.domain.auth.model.UserPrincipal;
import site.devtown.spadeworker.domain.auth.model.info.OAuth2UserInfo;
import site.devtown.spadeworker.domain.auth.model.info.OAuth2UserInfoFactory;
import site.devtown.spadeworker.domain.user.model.constant.AuthProviderType;
import site.devtown.spadeworker.domain.user.model.constant.UserRoleType;
import site.devtown.spadeworker.domain.user.model.constant.UserStatus;
import site.devtown.spadeworker.domain.user.model.entity.User;
import site.devtown.spadeworker.domain.user.model.entity.UserRole;
import site.devtown.spadeworker.domain.user.repository.RoleRepository;
import site.devtown.spadeworker.domain.user.repository.UserRepository;
import site.devtown.spadeworker.domain.user.repository.UserRoleRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

/**
 * OAuth2 기반으로 소셜 로그인 OR 회원가입을 구현하는 핵심 로직
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService
        extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        try {
            return this.process(userRequest, user);
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalAuthenticationServiceException(e.getMessage(), e.getCause());
        }
    }

    /**
     * 메인 로직
     */
    private OAuth2User process(
            OAuth2UserRequest userRequest,
            OAuth2User oAuth2User
    ) {
        AuthProviderType providerType = AuthProviderType.valueOf(
                userRequest
                        .getClientRegistration()
                        .getRegistrationId()
                        .toUpperCase()
        );

        // Social Login Provider 에서 받아온 로그인 정보를 파싱
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                providerType,
                oAuth2User.getAttributes()
        );

        // 계정이 존재하지 않을 경우 회원 가입을 진행
        User user = userRepository.findByPersonalId(userInfo.getId())
                .orElseGet(() -> createUser(userInfo, providerType));
        List<UserRoleType> roles = userRoleRepository.findAllByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("사용자 권한이 존재하지 않습니다."))
                .stream()
                .map(r -> r.getRole().getRoleType())
                .toList();
        // 회원 가입 된 계정의 로그인 유형과 현재 로그인 한 유형이 일치한지 검증
        if (providerType != user.getProviderType()) {
            throw new OAuthProviderMissMatchException(
                    "이미 " + user.getProviderType() + "계정으로 가입하셨습니다. 해당 소셜 로그인으로 다시 시도해주세요."
            );
        }

        return UserPrincipal.from(user, roles, oAuth2User.getAttributes());
    }

    /**
     * 소셜 로그인을 통해 회원 가입을 진행하는 로직
     */
    private User createUser(
            OAuth2UserInfo userInfo,
            AuthProviderType providerType
    ) {
        User savedUser = userRepository.saveAndFlush(
                User.of(
                        userInfo.getId(),
                        generatePassword(),
                        userInfo.getName(),
                        userInfo.getEmail(),
                        providerType,
                        UserStatus.ACTIVE
                )
        );

        // 사용자 권한 설정 -> 일반 사용자
        userRoleRepository.saveAndFlush(
                UserRole.of(
                        roleRepository.findByRoleType(UserRoleType.USER)
                                .orElseThrow(() -> new EntityNotFoundException("잘못된 권한입니다.")),
                        savedUser
                )
        );

        return savedUser;
    }

    /**
     * 임의적 Password 생성
     */
    private String generatePassword() {
        return passwordEncoder.encode(
                UUID.randomUUID().toString()
        );
    }
}

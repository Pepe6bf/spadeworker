package site.devtown.spadeworker.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import site.devtown.spadeworker.domain.user.model.entity.User;
import site.devtown.spadeworker.domain.user.repository.UserRepository;
import site.devtown.spadeworker.global.exception.ResourceNotFoundException;

import static site.devtown.spadeworker.domain.user.exception.UserExceptionCode.USER_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    /**
     * 현재 인증된 사용자의 User Entity 를 조회 후 반환
     */
    public User getCurrentAuthorizedUser() {
        String authenticationUserPersonalId = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByPersonalId(authenticationUserPersonalId)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
    }
}
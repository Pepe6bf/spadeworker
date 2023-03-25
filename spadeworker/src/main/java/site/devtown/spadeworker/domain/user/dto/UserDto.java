package site.devtown.spadeworker.domain.user.dto;

import site.devtown.spadeworker.domain.user.model.entity.User;

import java.time.LocalDateTime;

public record UserDto(
        String nickname,
        String email,
        String profileImageUrl,
        String introduce,
        String loginProviderType,
        LocalDateTime createdAt
) {
    public static UserDto of(
            String nickname,
            String email,
            String profileImageUrl,
            String introduce,
            String loginProviderType,
            LocalDateTime createdAt
    ) {
        return new UserDto(
                nickname,
                email,
                profileImageUrl,
                introduce,
                loginProviderType,
                createdAt
        );
    }

    public static UserDto from(User user) {
        return new UserDto(
                user.getNickname(),
                user.getEmail(),
                user.getProfileImagePath(),
                user.getIntroduce(),
                user.getProviderType().toString(),
                user.getCreatedAt()
        );
    }
}

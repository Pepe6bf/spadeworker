package site.devtown.spadeworker.domain.project.dto;

import site.devtown.spadeworker.domain.project.entity.Project;
import site.devtown.spadeworker.domain.user.model.entity.User;

import java.time.LocalDateTime;

public record ProjectDto(
        Long projectId,
        String title,
        String description,
        String thumbnailImageUri,
        int likeCount,
        int subscriberCount,
        UserInfo user,
        LocalDateTime createdAt
) {
    public static ProjectDto from(
            Project project,
            int likeCount,
            int subscriberCount
    ) {
        return new ProjectDto(
                project.getId(),
                project.getTitle(),
                project.getDescription(),
                project.getThumbnailImageUri(),
                likeCount,
                subscriberCount,
                UserInfo.from(project.getUser()),
                project.getCreatedAt()
        );
    }

    // ProjectDto 내부에서만 사용하는 UserDto
    private record UserInfo(
            Long userId,
            String nickname,
            String profileImageUri
    ) {
        private static UserInfo from(User user) {
            return new UserInfo(
                    user.getId(),
                    user.getNickname(),
                    user.getProfileImageUri()
            );
        }
    }
}
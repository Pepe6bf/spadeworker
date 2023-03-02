package site.devtown.spadeworker.domain.project.dto;

import site.devtown.spadeworker.domain.project.entity.Project;
import site.devtown.spadeworker.domain.user.model.entity.User;

import java.time.LocalDateTime;

public record ProjectDto(
        Long projectId,
        String title,
        String description,
        String thumbnailImageUri,
        UserDto user,
        LocalDateTime createdAt
) {
    public static ProjectDto from(Project project) {
        return new ProjectDto(
                project.getId(),
                project.getTitle(),
                project.getDescription(),
                project.getThumbnailImageUri(),
                UserDto.from(project.getUser()),
                project.getCreatedAt()
        );
    }

    // ProjectDto 내부에서만 사용하는 UserDto
    private record UserDto(
            Long userId,
            String nickname,
            String profileImageUri
    ) {
        private static UserDto from(User user) {
            return new UserDto(
                    user.getId(),
                    user.getNickname(),
                    user.getProfileImageUri()
            );
        }
    }
}
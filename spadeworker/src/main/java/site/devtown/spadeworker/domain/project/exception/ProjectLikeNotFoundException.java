package site.devtown.spadeworker.domain.project.exception;

public class ProjectLikeNotFoundException extends RuntimeException {
    public ProjectLikeNotFoundException() {
        super("프로젝트에 해당 사용자의 좋아요가 존재하지 않습니다.");
    }
}
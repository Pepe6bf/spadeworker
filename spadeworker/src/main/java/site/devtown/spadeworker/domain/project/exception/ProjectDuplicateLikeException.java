package site.devtown.spadeworker.domain.project.exception;

public class ProjectDuplicateLikeException extends RuntimeException {
    public ProjectDuplicateLikeException() {
        super("프로젝트에 이미 해당 사용자의 좋아요가 존재합니다.");
    }
}

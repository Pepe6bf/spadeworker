package site.devtown.spadeworker.domain.project.exception;

import static site.devtown.spadeworker.domain.project.exception.ProjectExceptionCode.*;

public class ProjectDuplicateLikeException extends RuntimeException {
    public ProjectDuplicateLikeException() {
        super(PROJECT_DUPLICATE_LIKE.getMessage());
    }
}

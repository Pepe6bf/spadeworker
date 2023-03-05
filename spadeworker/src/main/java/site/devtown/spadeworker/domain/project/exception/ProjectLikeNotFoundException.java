package site.devtown.spadeworker.domain.project.exception;

import static site.devtown.spadeworker.domain.project.exception.ProjectExceptionCode.*;

public class ProjectLikeNotFoundException extends RuntimeException {
    public ProjectLikeNotFoundException() {
        super(PROJECT_LIKE_NOT_FOUND.getMessage());
    }
}
package site.devtown.spadeworker.domain.project.exception;

import static site.devtown.spadeworker.domain.project.exception.ProjectExceptionCode.*;

public class ProjectDuplicateSubscribeException extends RuntimeException {
    public ProjectDuplicateSubscribeException() {
        super(PROJECT_DUPLICATE_SUBSCRIBE.getMessage());
    }
}

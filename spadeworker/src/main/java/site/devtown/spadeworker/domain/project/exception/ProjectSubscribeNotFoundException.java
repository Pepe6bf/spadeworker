package site.devtown.spadeworker.domain.project.exception;

import static site.devtown.spadeworker.domain.project.exception.ProjectExceptionCode.*;

public class ProjectSubscribeNotFoundException extends RuntimeException {
    public ProjectSubscribeNotFoundException() {
        super(PROJECT_SUBSCRIBE_NOT_FOUND.getMessage());
    }
}

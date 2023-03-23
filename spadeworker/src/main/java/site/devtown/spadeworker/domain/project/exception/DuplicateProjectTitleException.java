package site.devtown.spadeworker.domain.project.exception;

import static site.devtown.spadeworker.domain.project.exception.ProjectExceptionCode.DUPLICATE_PROJECT_TITLE;

public class DuplicateProjectTitleException
        extends RuntimeException {
    public DuplicateProjectTitleException() {
        super(DUPLICATE_PROJECT_TITLE.getMessage());
    }
}

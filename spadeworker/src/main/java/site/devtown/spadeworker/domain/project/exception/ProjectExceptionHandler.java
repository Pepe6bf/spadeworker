package site.devtown.spadeworker.domain.project.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import site.devtown.spadeworker.global.exception.ExceptionResponse;

import static site.devtown.spadeworker.domain.project.exception.ProjectExceptionCode.*;
import static site.devtown.spadeworker.domain.project.exception.ProjectExceptionCode.PROJECT_DUPLICATE_SUBSCRIBE;

@Slf4j
@RestControllerAdvice
public class ProjectExceptionHandler {

    /**
     * ProjectDuplicateLikeException 핸들링
     */
    @ExceptionHandler(ProjectDuplicateLikeException.class)
    public ResponseEntity<ExceptionResponse> handleProjectDuplicateLikeException(
            ProjectDuplicateLikeException e
    ) {
        log.error("{}", e.getMessage());
        return new ResponseEntity<>(
                ExceptionResponse.of(PROJECT_DUPLICATE_LIKE, PROJECT_DUPLICATE_LIKE.getMessage()),
                HttpStatus.valueOf(PROJECT_DUPLICATE_LIKE.getHttpStatus().value())
        );
    }

    /**
     * ProjectDuplicateSubscribeException 핸들링
     */
    @ExceptionHandler(ProjectLikeNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleProjectLikeNotFoundException(
            ProjectLikeNotFoundException e
    ) {
        log.error("{}", e.getMessage());
        return new ResponseEntity<>(
                ExceptionResponse.of(PROJECT_NOT_FOUND, PROJECT_NOT_FOUND.getMessage()),
                HttpStatus.valueOf(PROJECT_NOT_FOUND.getHttpStatus().value())
        );
    }

    /**
     * ProjectDuplicateSubscribeException 핸들링
     */
    @ExceptionHandler(ProjectDuplicateSubscribeException.class)
    public ResponseEntity<ExceptionResponse> handleProjectDuplicateSubscribeException(
            ProjectDuplicateSubscribeException e
    ) {
        log.error("{}", e.getMessage());
        return new ResponseEntity<>(
                ExceptionResponse.of(PROJECT_DUPLICATE_SUBSCRIBE, PROJECT_DUPLICATE_SUBSCRIBE.getMessage()),
                HttpStatus.valueOf(PROJECT_DUPLICATE_SUBSCRIBE.getHttpStatus().value())
        );
    }

    /**
     * ProjectSubscribeNotFoundException 핸들링
     */
    @ExceptionHandler(ProjectSubscribeNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleProjectSubscribeNotFoundException(
            ProjectSubscribeNotFoundException e
    ) {
        log.error("{}", e.getMessage());
        return new ResponseEntity<>(
                ExceptionResponse.of(PROJECT_SUBSCRIBE_NOT_FOUND, PROJECT_SUBSCRIBE_NOT_FOUND.getMessage()),
                HttpStatus.valueOf(PROJECT_SUBSCRIBE_NOT_FOUND.getHttpStatus().value())
        );
    }

    /**
     * DuplicateProjectTitleException 핸들링
     */
    @ExceptionHandler(DuplicateProjectTitleException.class)
    public ResponseEntity<ExceptionResponse> handleDuplicateProjectTitleException(
            DuplicateProjectTitleException e
    ) {
        log.error("{}", e.getMessage());
        return new ResponseEntity<>(
                ExceptionResponse.of(DUPLICATE_PROJECT_TITLE, DUPLICATE_PROJECT_TITLE.getMessage()),
                HttpStatus.valueOf(DUPLICATE_PROJECT_TITLE.getHttpStatus().value())
        );
    }
}
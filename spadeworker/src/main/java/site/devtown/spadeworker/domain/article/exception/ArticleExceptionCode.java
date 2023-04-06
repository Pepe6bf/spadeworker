package site.devtown.spadeworker.domain.article.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import site.devtown.spadeworker.global.exception.ExceptionCode;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum ArticleExceptionCode
        implements ExceptionCode {

    ARTICLE_NOT_FOUND(NOT_FOUND, "AC-C-001", "존재하지 않는 게시글입니다."),
    INVALID_ARTICLE_OWNER(FORBIDDEN, "AC-C-002", "게시글을 처리할 권한이 없습니다."),
    ARTICLE_EMOTION_NOT_FOUND(NOT_FOUND, "AC-C-003", "게시글에 해당 사용자의 반응이 존재하지 않습니다."),
    ARTICLE_DUPLICATE_SCRAP(BAD_REQUEST, "AC-C-004", "이미 해당 게시물을 스크랩 중 입니다."),
    NOT_SCRAPED_ARTICLE(BAD_REQUEST, "AC-C-005", "스크랩 중이 아닌 게시글입니다."),
    TEMP_ARTICLE_NOT_FOUND(NOT_FOUND, "AC-C-006", "존재하지 않는 임시 게시글입니다."),
    HASHTAG_NOT_FOUND(NOT_FOUND, "AC-C-007", "존재하지 않는 해시태그입니다."),
    TEMP_ARTICLE_THUMBNAIL_IMAGE_NOT_FOUND(NOT_FOUND, "AC-C-008", "존재하지 않는 게시글 썸네일 이미지 입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}

package site.devtown.spadeworker.domain.file.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImageFileType {

    USER_PROFILE_IMAGE("user-profile", "사용자 프로필 이미지"),
    PROJECT_THUMBNAIL_IMAGE("project-thumbnail", "프로젝트 썸네일 이미지"),
    ARTICLE_THUMBNAIL_IMAGE("article-thumbnail", "게시글 썸네일 이미지"),
    ARTICLE_CONTENT_IMAGE("article-content", "게시글 컨텐츠 이미지"),
    ARTICLE_COMMENT_CONTENT_IMAGE("article-comment-content", "게시글 댓글 컨텐츠 이미지");

    private final String imageType;
    private final String description;
}

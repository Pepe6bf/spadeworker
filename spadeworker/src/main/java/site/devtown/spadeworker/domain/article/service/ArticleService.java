package site.devtown.spadeworker.domain.article.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.devtown.spadeworker.domain.article.dto.ArticleIdResponse;
import site.devtown.spadeworker.domain.article.dto.SaveTempArticleRequest;
import site.devtown.spadeworker.domain.article.dto.TempArticleDto;
import site.devtown.spadeworker.domain.article.model.entity.Article;
import site.devtown.spadeworker.domain.article.repository.ArticleRepository;
import site.devtown.spadeworker.domain.project.service.ProjectService;
import site.devtown.spadeworker.domain.user.model.entity.User;
import site.devtown.spadeworker.domain.user.service.UserService;
import site.devtown.spadeworker.global.exception.InvalidResourceOwnerException;
import site.devtown.spadeworker.global.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static site.devtown.spadeworker.domain.article.exception.ArticleExceptionCode.*;
import static site.devtown.spadeworker.domain.article.model.constant.ArticleStatus.TEMP;

@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserService userService;
    private final ProjectService projectService;
    private final ArticleImageService articleImageService;
    private final HashtagService hashtagService;

    @Value("${image.article-thumbnail-image.default-image-full-path}")
    private String defaultArticleThumbnailImageFullPath;

    /**
     * 게시글 생성 - 임시 저장
     */
    public ArticleIdResponse saveTempArticle(
            Long projectId,
            Optional<Long> articleId,
            SaveTempArticleRequest request
    ) {

        Article article;

        // articleId가 요청에 없을 경우 게시글 임시 저장을 처음 하는 것으로 판단
        if (articleId.isEmpty()) {
            article = articleRepository.save(
                    Article.of(
                            request.title(),
                            request.content(),
                            defaultArticleThumbnailImageFullPath,
                            TEMP,
                            projectService.getProjectEntity(projectId),
                            userService.getCurrentAuthorizedUser()
                    )
            );
            // 기존에 임시 저장된 게시글이 존재할 경우
        } else {
            article = getArticleEntity(articleId.get());

            validateArticleOwner(article.getUser());

            article.update(
                    request.title(),
                    request.content()
            );
        }

        hashtagService.updateArticleHashtags(
                request.hashtags(),
                article
        );

        return ArticleIdResponse.of(article.getId());
    }

    /**
     * 임시 저장 게시글 조회 - 단건 조회
     */
    public TempArticleDto getTempArticle(
            Long articleId
    ) {

        return articleRepository.findByIdAndStatus(articleId, TEMP)
                .map(article -> TempArticleDto.from(
                                article,
                                hashtagService.getAllArticleHashtags(article)
                        )
                )
                .orElseThrow(() -> new ResourceNotFoundException(TEMP_ARTICLE_NOT_FOUND));
    }

    /**
     * 임시 저장 게시글 조회 - 전체 조회
     */
    public List<TempArticleDto> getTempArticles() {
        return articleRepository.findByUserAndStatus(
                        userService.getCurrentAuthorizedUser(),
                        TEMP
                )
                .stream()
                .map(article -> TempArticleDto.from(
                        article,
                        hashtagService.getAllArticleHashtags(article)
                ))
                .toList();
    }

    /**
     * 임시 게시글 삭제
     */
    public void deleteTempArticle(
            Long articleId
    ) {

        Article article = getArticleEntity(articleId);

        validateArticleOwner(article.getUser());

        articleImageService.deleteArticleContentImages(article);
        hashtagService.deleteArticleHashtags(article);
        articleRepository.delete(article);
    }

    /**
     * Article Entity 조회
     */
    public Article getArticleEntity(
            Long articleId
    ) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException(ARTICLE_NOT_FOUND));
    }

    // (임시)게시글 소유자와 현재 인가된 사용자가 동일한지 검증
    private void validateArticleOwner(User owner) {
        if (!Objects.equals(owner, userService.getCurrentAuthorizedUser())) {
            throw new InvalidResourceOwnerException(INVALID_ARTICLE_OWNER);
        }
    }
}
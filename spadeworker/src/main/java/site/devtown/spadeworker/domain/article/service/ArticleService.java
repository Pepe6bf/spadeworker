package site.devtown.spadeworker.domain.article.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.devtown.spadeworker.domain.article.dto.CreateArticleRequest;
import site.devtown.spadeworker.domain.article.dto.CreateArticleResponse;
import site.devtown.spadeworker.domain.article.model.entity.*;
import site.devtown.spadeworker.domain.article.repository.*;
import site.devtown.spadeworker.domain.file.dto.UploadSingleImageResponse;
import site.devtown.spadeworker.domain.file.service.AmazonS3ImageService;
import site.devtown.spadeworker.domain.project.service.ProjectService;
import site.devtown.spadeworker.domain.user.service.UserService;
import site.devtown.spadeworker.global.exception.ResourceNotFoundException;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

import static site.devtown.spadeworker.domain.article.exception.ArticleExceptionCode.HASHTAG_NOT_FOUND;
import static site.devtown.spadeworker.domain.file.constant.ImageFileType.ARTICLE_CONTENT_IMAGE;
import static site.devtown.spadeworker.domain.file.constant.ImageFileType.ARTICLE_THUMBNAIL_IMAGE;

@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleContentImageRepository articleContentImageRepository;
    private final HashtagRepository hashtagRepository;
    private final ArticleHashtagRepository articleHashtagRepository;
    private final UserService userService;
    private final AmazonS3ImageService amazonS3ImageService;
    private final ProjectService projectService;
    private final TempArticleService tempArticleService;

    @Value("${image.article-thumbnail-image.default-image-path}")
    private String defaultArticleThumbnailImagePath;

    /**
     * 게시글 컨텐츠 이미지 업로드 비즈니스 로직
     */
    public UploadSingleImageResponse saveArticleContentImage(
            Long tempArticleId,
            MultipartFile requestImage
    ) throws IOException {

        // S3 에 게시글 본문 이미지 업로드
        String storedImageFullPath = amazonS3ImageService.uploadImage(
                ARTICLE_CONTENT_IMAGE,
                requestImage
        );

        tempArticleService.saveTempArticleContentImage(
                storedImageFullPath,
                tempArticleId
        );

        return UploadSingleImageResponse.of(
                requestImage.getOriginalFilename(),
                storedImageFullPath
        );
    }

    /**
     * 게시글 썸네일 이미지 업로드 비즈니스 로직
     */
    public UploadSingleImageResponse uploadArticleThumbnailImage(
            Long tempArticleId,
            MultipartFile requestImage
    ) throws IOException {

        TempArticle tempArticle = tempArticleService.getTempArticleEntity(tempArticleId);

        tempArticleService.deleteTempArticleThumbnailImageWithS3(tempArticle.getId());

        // S3 에 게시글 본문 이미지 업로드
        String storedImageFullPath = amazonS3ImageService.uploadImage(
                ARTICLE_THUMBNAIL_IMAGE,
                requestImage
        );

        tempArticleService.saveTempArticleThumbnailImage(storedImageFullPath, tempArticle);

        return UploadSingleImageResponse.of(
                requestImage.getOriginalFilename(),
                storedImageFullPath
        );
    }

    /**
     * 게시글 생성 비즈니스 로직
     */
    public CreateArticleResponse createArticle(
            Long tempArticleId,
            CreateArticleRequest request
    ) {

        TempArticle tempArticle = tempArticleService.getTempArticleEntity(tempArticleId);
        // Article Entity 생성
        Article article = articleRepository.save(
                Article.of(
                        request.title(),
                        request.content(),
                        getArticleThumbnailImagePath(tempArticle),
                        request.status(),
                        projectService.getProjectEntity(request.projectId()),
                        userService.getCurrentAuthorizedUser()
                )
        );

        saveTempArticleContentImageToArticleContentImage(
                tempArticle,
                article
        );

        saveArticleHashtags(
                request.hashtags(),
                article
        );

        // 임시 게시글 및 관련 정보 제거
        tempArticleService.clearTempArticle(tempArticle);

        return CreateArticleResponse.of(
                article.getId()
        );
    }

    // 게시글 썸네일 이미지 설정
    private String getArticleThumbnailImagePath(
            TempArticle tempArticle
    ) {
        Optional<TempArticleThumbnailImage> tempArticleThumbnailImage
                = tempArticleService.getTempArticleThumbnailImage(tempArticle);

        return (tempArticleThumbnailImage.isPresent()) ?
                tempArticleThumbnailImage.get().getImagePath() :
                defaultArticleThumbnailImagePath;
    }

    // 임시 게시글 본문 이미지를 모두 본 게시글로 이동
    public void saveTempArticleContentImageToArticleContentImage(
            TempArticle tempArticle,
            Article article
    ) {
        tempArticleService.getTempArticleContentImages(tempArticle)
                .forEach(
                        tempImage -> {
                            articleContentImageRepository.save(
                                    ArticleContentImage.of(
                                            tempImage.getImagePath(),
                                            article
                                    )
                            );
                        }
                );
    }

    public void saveArticleHashtags(
            Set<String> hashtags,
            Article article
    ) {
        hashtags.forEach(
                hashtag -> {
                    createHashtag(hashtag);
                    createArticleHashtag(
                            getHashtagEntity(hashtag),
                            article
                    );
                }
        );
    }

    private void createHashtag(
            String hashtag
    ) {
        if (!hashtagRepository.existsByTitle(hashtag)) {
            hashtagRepository.save(
                    Hashtag.of(hashtag)
            );
        }
    }

    private void createArticleHashtag(
            Hashtag hashtag,
            Article article
    ) {
        articleHashtagRepository.save(
                ArticleHashtag.of(
                        hashtag,
                        article
                )
        );
    }

    private Hashtag getHashtagEntity(
            String hashtag
    ) {
        return hashtagRepository.findByTitle(hashtag)
                .orElseThrow(() -> new ResourceNotFoundException(HASHTAG_NOT_FOUND));
    }
}
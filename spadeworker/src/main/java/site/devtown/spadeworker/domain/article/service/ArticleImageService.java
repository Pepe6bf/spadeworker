package site.devtown.spadeworker.domain.article.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.devtown.spadeworker.domain.article.model.entity.Article;
import site.devtown.spadeworker.domain.article.model.entity.ArticleContentImage;
import site.devtown.spadeworker.domain.article.repository.ArticleContentImageRepository;
import site.devtown.spadeworker.domain.article.repository.ArticleRepository;
import site.devtown.spadeworker.domain.file.dto.UploadSingleImageResponse;
import site.devtown.spadeworker.domain.file.exception.ImageFileNotFoundException;
import site.devtown.spadeworker.domain.file.service.AmazonS3ImageService;
import site.devtown.spadeworker.global.exception.ResourceNotFoundException;

import java.io.IOException;

import static site.devtown.spadeworker.domain.article.exception.ArticleExceptionCode.ARTICLE_NOT_FOUND;
import static site.devtown.spadeworker.domain.file.constant.ImageFileType.ARTICLE_CONTENT_IMAGE;
import static site.devtown.spadeworker.domain.file.constant.ImageFileType.ARTICLE_THUMBNAIL_IMAGE;

@RequiredArgsConstructor
@Transactional
@Service
public class ArticleImageService {

    private final ArticleRepository articleRepository;
    private final ArticleContentImageRepository articleContentImageRepository;
    private final AmazonS3ImageService amazonS3ImageService;

    @Value("${image.article-thumbnail-image.default-image-full-path}")
    private String defaultArticleThumbnailImageFullPath;

    /**
     * 게시글 컨텐츠 이미지 업로드 비즈니스 로직
     */
    public UploadSingleImageResponse saveArticleContentImage(
            Long articleId,
            MultipartFile requestImage
    ) throws IOException {

        // S3 에 게시글 본문 이미지 업로드
        String storedImageFullPath = amazonS3ImageService.uploadImage(
                ARTICLE_CONTENT_IMAGE,
                requestImage
        );

        // DB 에 게시글 본문 이미지 정도 저장
        articleContentImageRepository.save(
                ArticleContentImage.of(
                        storedImageFullPath,
                        getArticleEntity(articleId)
                )
        );

        return UploadSingleImageResponse.of(
                requestImage.getOriginalFilename(),
                storedImageFullPath
        );
    }

    /**
     * 게시글 본문 이미지 S3 및 DB 삭제 비즈니스 로직
     */
    public void deleteArticleContentImages(
            Article article
    ) {
        articleContentImageRepository.findAllByArticle(article)
                .forEach(articleContentImage -> {
                    // S3에 존재하는 이미지 삭제
                    amazonS3ImageService.deleteImage(articleContentImage.getImagePath());
                    articleContentImageRepository.delete(articleContentImage);
                });
    }

    /**
     * 게시글 썸네일 이미지 업로드 비즈니스 로직
     */
    public UploadSingleImageResponse uploadArticleThumbnailImage(
            Long articleId,
            MultipartFile requestImage
    ) throws IOException {

        Article article = getArticleEntity(articleId);

        // 기존 S3에 이미지가 존재한다면 삭제
        deleteArticleThumbnailImage(article.getThumbnailImagePath());

        // S3 에 게시글 본문 이미지 업로드
        String storedImageFullPath = amazonS3ImageService.uploadImage(
                ARTICLE_THUMBNAIL_IMAGE,
                requestImage
        );

        return UploadSingleImageResponse.of(
                requestImage.getOriginalFilename(),
                storedImageFullPath
        );
    }

    /**
     * 게시글 썸네일 이미지 삭제 비즈니스 로직
     */
    public void deleteArticleThumbnailImage(
            String articleThumbnailFullPath
    ) {
        try {
            if (!articleThumbnailFullPath.equals(defaultArticleThumbnailImageFullPath))
                amazonS3ImageService.deleteImage(articleThumbnailFullPath);
        } catch (ImageFileNotFoundException e) {
            return;
        }
    }

    private Article getArticleEntity(
            Long articleId
    ) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException(ARTICLE_NOT_FOUND));
    }
}
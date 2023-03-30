package site.devtown.spadeworker.domain.article.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.devtown.spadeworker.domain.article.dto.SaveTempArticleRequest;
import site.devtown.spadeworker.domain.article.dto.SaveTempArticleResponse;
import site.devtown.spadeworker.domain.article.dto.TempArticleDto;
import site.devtown.spadeworker.domain.article.model.entity.TempArticle;
import site.devtown.spadeworker.domain.article.model.entity.TempArticleContentImage;
import site.devtown.spadeworker.domain.article.model.entity.TempArticleHashtag;
import site.devtown.spadeworker.domain.article.model.entity.TempArticleThumbnailImage;
import site.devtown.spadeworker.domain.article.repository.TempArticleContentImageRepository;
import site.devtown.spadeworker.domain.article.repository.TempArticleHashtagRepository;
import site.devtown.spadeworker.domain.article.repository.TempArticleRepository;
import site.devtown.spadeworker.domain.article.repository.TempArticleThumbnailImageRepository;
import site.devtown.spadeworker.domain.file.service.AmazonS3ImageService;
import site.devtown.spadeworker.domain.user.service.UserService;
import site.devtown.spadeworker.global.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static site.devtown.spadeworker.domain.article.exception.ArticleExceptionCode.TEMP_ARTICLE_NOT_FOUND;
import static site.devtown.spadeworker.domain.article.exception.ArticleExceptionCode.TEMP_ARTICLE_THUMBNAIL_IMAGE_NOT_FOUND;

@RequiredArgsConstructor
@Transactional
@Service
public class TempArticleService {

    private final TempArticleRepository tempArticleRepository;
    private final TempArticleContentImageRepository tempArticleContentImageRepository;
    private final TempArticleHashtagRepository tempArticleHashtagRepository;
    private final TempArticleThumbnailImageRepository tempArticleThumbnailImageRepository;
    private final UserService userService;
    private final AmazonS3ImageService amazonS3ImageService;

    /**
     * 임시 게시글 저장 비즈니스 로직
     */
    public SaveTempArticleResponse saveTempArticle(
            Optional<Long> tempArticleId,
            SaveTempArticleRequest request
    ) {

        TempArticle tempArticle;

        // tempArticleId가 존재하지 않을 경우 임시 게시글을 처음 생성하는 것으로 판단
        if (tempArticleId.isEmpty()) {
            tempArticle = tempArticleRepository.save(
                    TempArticle.of(
                            request.title(),
                            request.content(),
                            userService.getCurrentAuthorizedUser()
                    )
            );
        } else {
            tempArticle = getTempArticleEntity(tempArticleId.get());
            tempArticle.update(
                    request.title(),
                    request.content()
            );
        }

        // 임시 게시글 해시태그 업데이트
        updateTempArticleHashtags(
                request.hashtags(),
                tempArticle
        );

        return SaveTempArticleResponse.of(tempArticle.getId());
    }

    // 임시 게시글의 해시태그를 업데이트
    private void updateTempArticleHashtags(
            Set<String> hashtags,
            TempArticle tempArticle
    ) {

        // 기존 해시태그 제거
        tempArticleHashtagRepository.deleteAllByTempArticle(tempArticle);

        // 해시태그 업데이트
        hashtags.forEach(
                tempArticleHashtag -> {
                    tempArticleHashtagRepository.save(
                            TempArticleHashtag.of(tempArticleHashtag, tempArticle)
                    );
                }
        );
    }

    /**
     * 임시 게시글 전체 조회
     */
    @Transactional(readOnly = true)
    public List<TempArticleDto> getTempArticles() {
        return tempArticleRepository.findAllByUser(userService.getCurrentAuthorizedUser())
                .stream().map(
                        tempArticle -> {
                            Set<String> hashtags = tempArticleHashtagRepository.findAllByTempArticle(tempArticle)
                                    .stream().map(TempArticleHashtag::getTitle).collect(Collectors.toSet());
                            return TempArticleDto.from(
                                    tempArticle,
                                    hashtags
                            );
                        }
                ).toList();
    }

    /**
     * 임시 게시글 단건 조회
     */
    @Transactional(readOnly = true)
    public TempArticleDto getTempArticle(
            Long tempArticleId
    ) {
        TempArticle tempArticle = tempArticleRepository.findById(tempArticleId)
                .orElseThrow(() -> new ResourceNotFoundException(TEMP_ARTICLE_NOT_FOUND));

        return TempArticleDto.from(
                tempArticle,
                tempArticleHashtagRepository.findAllByTempArticle(tempArticle)
                        .stream().map(TempArticleHashtag::getTitle).collect(Collectors.toSet())
        );
    }

    /**
     * 임시 게시글 및 연관 정보 삭제
     */
    public void deleteTempArticle(
            Long tempArticleId
    ) {
        clearTempArticleWithS3(getTempArticleEntity(tempArticleId));
    }

    // 임시 게시글에 연관된 해시태그 전체 삭제
    private void deleteAllTempArticleHashtags(
            TempArticle tempArticle
    ) {
        tempArticleHashtagRepository.deleteAllByTempArticle(tempArticle);
    }

    // 임시 게시글에 연관된 본문 이미지 전체 삭제
    private void deleteAllTempArticleContentImages(
            TempArticle tempArticle
    ) {
        // DB에 존재하는 이미지 정보 삭제
        tempArticleContentImageRepository.deleteAll(
                tempArticleContentImageRepository.findAllByTempArticle(tempArticle)
        );
    }

    // 임시 게시글에 연관된 본문 이미지 전체 삭제 및 S3 정리
    private void deleteAllTempArticleContentImagesWithS3(
            TempArticle tempArticle
    ) {
        tempArticleContentImageRepository.findAllByTempArticle(tempArticle)
                .forEach(tempArticleContentImage -> {
                    // S3에 존재하는 이미지 삭제
                    amazonS3ImageService.deleteImage(tempArticleContentImage.getImagePath());
                    tempArticleContentImageRepository.delete(tempArticleContentImage);
                });
    }

    // 임시 게시글 썸네일 삭제
    private void deleteTempArticleThumbnailImage(
            TempArticle tempArticle
    ) {
        tempArticleThumbnailImageRepository.deleteAllByTempArticle(tempArticle);
    }

    /**
     * 임시 게시글 썸네일 삭제 및 S3정리
     */
    public void deleteTempArticleThumbnailImageWithS3(
            Long tempArticleId
    ) {
        TempArticle tempArticle = getTempArticleEntity(tempArticleId);

        if (tempArticleThumbnailImageRepository.existsByTempArticle(tempArticle)) {
            TempArticleThumbnailImage tempArticleThumbnailImage
                    = tempArticleThumbnailImageRepository.findByTempArticle(tempArticle)
                    .orElseThrow(() -> new ResourceNotFoundException(TEMP_ARTICLE_THUMBNAIL_IMAGE_NOT_FOUND));

            amazonS3ImageService.deleteImage(
                    tempArticleThumbnailImage.getImagePath()
            );

            deleteTempArticleThumbnailImage(tempArticle);
        }
    }

    /**
     * 임시 게시글 DB 정보만 삭제
     */
    public void clearTempArticle(
            TempArticle tempArticle
    ) {
        deleteAllTempArticleHashtags(tempArticle);
        deleteAllTempArticleContentImages(tempArticle);
        deleteTempArticleThumbnailImage(tempArticle);

        tempArticleRepository.delete(tempArticle);
    }

    // 임시 게시글 DB 정보 삭제 및 S3 정리
    private void clearTempArticleWithS3(
            TempArticle tempArticle
    ) {
        deleteAllTempArticleHashtags(tempArticle);
        deleteAllTempArticleContentImagesWithS3(tempArticle);
        deleteTempArticleThumbnailImageWithS3(tempArticle.getId());

        tempArticleRepository.delete(tempArticle);
    }

    /**
     * TempArticle Entity 조회 및 반환
     */
    public TempArticle getTempArticleEntity(
            Long tempArticleId
    ) {
        return tempArticleRepository.findById(tempArticleId)
                .orElseThrow(() -> new ResourceNotFoundException(TEMP_ARTICLE_NOT_FOUND));
    }

    /**
     * 임시 게시글의 본문 이미지 전체 조회
     */
    public List<TempArticleContentImage> getTempArticleContentImages(
            TempArticle tempArticle
    ) {
        return tempArticleContentImageRepository.findAllByTempArticle(tempArticle);
    }

    /**
     * 임시 게시글의 썸네일 이미지 조회
     */
    public Optional<TempArticleThumbnailImage> getTempArticleThumbnailImage(
            TempArticle tempArticle
    ) {
        return tempArticleThumbnailImageRepository.findByTempArticle(tempArticle);
    }

    /**
     * 임시 게시글 본문 이미지 저장
     */
    public void saveTempArticleContentImage(
            String storedImageFullPath,
            Long tempArticleId

    ) {
        tempArticleContentImageRepository.save(
                TempArticleContentImage.of(
                        storedImageFullPath,
                        getTempArticleEntity(tempArticleId)
                )
        );
    }

    /**
     * 임시 게시글 썸네일 이미지 저장
     */
    public void saveTempArticleThumbnailImage(
            String storedImageFullPath,
            TempArticle tempArticle
    ) {
        tempArticleThumbnailImageRepository.save(
                TempArticleThumbnailImage.of(
                        storedImageFullPath,
                        tempArticle
                )
        );
    }
}
package site.devtown.spadeworker.domain.article.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.devtown.spadeworker.domain.article.dto.SaveTempArticleRequest;
import site.devtown.spadeworker.domain.article.dto.SaveTempArticleResponse;
import site.devtown.spadeworker.domain.article.dto.TempArticleDto;
import site.devtown.spadeworker.domain.article.model.entity.TempArticle;
import site.devtown.spadeworker.domain.article.model.entity.TempArticleContentImage;
import site.devtown.spadeworker.domain.article.model.entity.TempArticleHashtag;
import site.devtown.spadeworker.domain.article.repository.ArticleRepository;
import site.devtown.spadeworker.domain.article.repository.TempArticleContentImageRepository;
import site.devtown.spadeworker.domain.article.repository.TempArticleHashtagRepository;
import site.devtown.spadeworker.domain.article.repository.TempArticleRepository;
import site.devtown.spadeworker.domain.file.dto.UploadContentImageResponse;
import site.devtown.spadeworker.domain.file.service.AmazonS3ImageService;
import site.devtown.spadeworker.domain.user.model.entity.User;
import site.devtown.spadeworker.domain.user.service.UserService;
import site.devtown.spadeworker.global.exception.ResourceNotFoundException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static site.devtown.spadeworker.domain.article.exception.ArticleExceptionCode.TEMP_ARTICLE_NOT_FOUND;
import static site.devtown.spadeworker.domain.file.constant.ImageFileType.ARTICLE_CONTENT_IMAGE;

@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final TempArticleRepository tempArticleRepository;
    private final TempArticleContentImageRepository tempArticleContentImageRepository;
    private final TempArticleHashtagRepository tempArticleHashtagRepository;
    private final AmazonS3ImageService amazonS3ImageService;
    private final UserService userService;

    /**
     * 임시 게시글 저장 비즈니스 로직
     */
    public SaveTempArticleResponse saveTempArticle(
            Optional<Long> tempArticleId,
            SaveTempArticleRequest request
    ) {

        TempArticle tempArticle;

        // tempArticleId 가 존재하지 않을 경우 임시 게시글을 처음 생성하는 것으로 판단
        if (tempArticleId.isEmpty()) {
            tempArticle = tempArticleRepository.save(
                    TempArticle.of(
                            request.title(),
                            request.content(),
                            userService.getCurrentAuthorizedUser()
                    )
            );
        } else {
            tempArticle = tempArticleRepository.findById(tempArticleId.get())
                    .orElseThrow(() -> new ResourceNotFoundException(TEMP_ARTICLE_NOT_FOUND));

            tempArticle.update(
                    request.title(),
                    request.content()
            );
        }

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
                t -> {
                    tempArticleHashtagRepository.save(
                            TempArticleHashtag.of(t, tempArticle)
                    );
                }
        );
    }

    /**
     * 게시글 컨텐츠 이미지 업로드 비즈니스 로직
     */
    public UploadContentImageResponse saveArticleContentImage(
            Long tempArticleId,
            MultipartFile requestImage
    ) throws IOException {

        // S3 에 게시글 본문 이미지 업로드
        String storedImageFullPath = amazonS3ImageService.uploadImage(
                ARTICLE_CONTENT_IMAGE,
                requestImage
        );

        TempArticle tempArticle = tempArticleRepository.findById(tempArticleId)
                .orElseThrow(() -> new ResourceNotFoundException(TEMP_ARTICLE_NOT_FOUND));

        tempArticleContentImageRepository.save(
                TempArticleContentImage.of(
                        storedImageFullPath,
                        tempArticle
                )
        );

        return UploadContentImageResponse.of(
                requestImage.getOriginalFilename(),
                storedImageFullPath
        );
    }

    /**
     * 임시 게시글 전체 조회
     */
    public List<TempArticleDto> getTempArticles() {
        User currentAuthorizedUser = userService.getCurrentAuthorizedUser();

        return tempArticleRepository.findAllByUser(currentAuthorizedUser)
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
     * 임시 게시글 삭제
     */
    public void deleteTempArticle(
            Long tempArticleId
    ) {
        TempArticle tempArticle = tempArticleRepository.findById(tempArticleId)
                .orElseThrow(() -> new ResourceNotFoundException(TEMP_ARTICLE_NOT_FOUND));

        deleteAllTempArticleHashtags(tempArticle);
        deleteAllTempArticleContentImages(tempArticle);
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
        tempArticleContentImageRepository.findAllByTempArticle(tempArticle)
                .forEach(tempArticleContentImage -> {
                    // S3에 존재하는 이미지 삭제
                    amazonS3ImageService.deleteImage(tempArticleContentImage.getImagePath());

                    // DB에 존재하는 이미지 정보 삭제
                    tempArticleContentImageRepository.delete(tempArticleContentImage);
                });
    }
}
package site.devtown.spadeworker.domain.article.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.devtown.spadeworker.domain.article.dto.SaveTempArticleRequest;
import site.devtown.spadeworker.domain.article.dto.SaveTempArticleResponse;
import site.devtown.spadeworker.domain.article.model.entity.TempArticle;
import site.devtown.spadeworker.domain.article.model.entity.TempArticleContentImage;
import site.devtown.spadeworker.domain.article.model.entity.TempArticleHashtag;
import site.devtown.spadeworker.domain.article.repository.ArticleRepository;
import site.devtown.spadeworker.domain.article.repository.TempArticleContentImageRepository;
import site.devtown.spadeworker.domain.article.repository.TempArticleHashtagRepository;
import site.devtown.spadeworker.domain.article.repository.TempArticleRepository;
import site.devtown.spadeworker.domain.file.dto.UploadContentImageResponse;
import site.devtown.spadeworker.domain.file.service.AmazonS3ImageService;
import site.devtown.spadeworker.domain.user.service.UserService;
import site.devtown.spadeworker.global.exception.ResourceNotFoundException;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

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

    /**
     * 임시 게시글의 해시태그를 업데이트
     */
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
}
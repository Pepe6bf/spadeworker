package site.devtown.spadeworker.domain.article.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import site.devtown.spadeworker.domain.article.dto.ArticleIdResponse;
import site.devtown.spadeworker.domain.article.dto.SaveTempArticleRequest;
import site.devtown.spadeworker.domain.article.dto.TempArticleDto;
import site.devtown.spadeworker.domain.article.service.ArticleImageService;
import site.devtown.spadeworker.domain.article.service.ArticleService;
import site.devtown.spadeworker.domain.file.dto.UploadSingleImageRequest;
import site.devtown.spadeworker.domain.file.dto.UploadSingleImageResponse;
import site.devtown.spadeworker.domain.file.validation.ContentImageValidate;
import site.devtown.spadeworker.global.response.CommonResult;
import site.devtown.spadeworker.global.response.ListResult;
import site.devtown.spadeworker.global.response.ResponseService;
import site.devtown.spadeworker.global.response.SingleResult;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RequestMapping("/api/projects/{project-id}/articles")
@RestController
public class ArticleController {

    private final ArticleService articleService;
    private final ArticleImageService articleImageService;
    private final ResponseService responseService;

    /**
     * 임시 게시글 저장 API
     */
    @PutMapping("/temp-articles")
    public SingleResult<ArticleIdResponse> saveTempArticle(
            @PathVariable("project-id") Long projectId,
            @RequestParam("article-id") Optional<Long> articleId,
            @RequestBody @Valid SaveTempArticleRequest request
    ) {
        return responseService.getSingleResult(
                OK.value(),
                "성공적으로 게시글이 임시 저장되었습니다.",
                articleService.saveTempArticle(
                        projectId,
                        articleId,
                        request
                )
        );
    }

    /**
     * 임시 게시글 전체 조회 API
     */
    @GetMapping("/temp-articles")
    public ListResult<TempArticleDto> getTempArticles() {

        return responseService.getListResult(
                OK.value(),
                "성공적으로 임시 게시글이 조회되었습니다.",
                articleService.getTempArticles()
        );
    }

    /**
     * 임시 게시글 단건 조회 API
     */
    @GetMapping("/temp-articles/{article-id}")
    public SingleResult<TempArticleDto> getTempArticle(
            @PathVariable("article-id") Long articleId
    ) {

        return responseService.getSingleResult(
                OK.value(),
                "성공적으로 임시 게시글이 조회되었습니다.",
                articleService.getTempArticle(articleId)
        );
    }

    /**
     * 임시 게시글 삭제 API
     */
    @DeleteMapping("/temp-articles/{article-id}")
    public CommonResult deleteTempArticle(
            @PathVariable("article-id") Long articleId
    ) {

        articleService.deleteTempArticle(articleId);

        return responseService.getSuccessResult(
                OK.value(),
                "성공적으로 임시 게시글이 삭제되었습니다."
        );
    }

    /**
     * 게시글 본문 이미지 업로드 API
     */
    @PostMapping("/article-content-images")
    public SingleResult<UploadSingleImageResponse> saveArticleContentImage(
            @RequestParam("article-id") Long articleId,
            @ModelAttribute @Valid @ContentImageValidate UploadSingleImageRequest request
    ) throws IOException {
        return responseService.getSingleResult(
                OK.value(),
                "게시글 컨텐츠 이미지가 성공적으로 업로드 되었습니다.",
                articleImageService.saveArticleContentImage(
                        articleId,
                        request.image()
                )
        );
    }

    /**
     * 게시글 썸네일 이미지 업로드 API
     */
    @PutMapping("/article-thumbnail-images")
    public SingleResult<UploadSingleImageResponse> saveArticleThumbnailImage(
            @RequestParam("article-id") Long articleId,
            @ModelAttribute @Valid @ContentImageValidate UploadSingleImageRequest request
    ) throws IOException {
        return responseService.getSingleResult(
                OK.value(),
                "성공적으로 게시글 썸네일이 업로드 되었습니다.",
                articleImageService.uploadArticleThumbnailImage(
                        articleId,
                        request.image()
                )
        );
    }

    /**
     * 게시글 썸네일 이미지 삭제 API
     */
    @DeleteMapping("/article-thumbnail-images")
    public CommonResult deleteArticleThumbnailImage(
            @RequestParam("image-path") String imagePath
    ) {

        articleImageService.deleteArticleThumbnailImage(imagePath);

        return responseService.getSuccessResult(
                OK.value(),
                "성공적으로 게시글 썸네일 이미지를 제거하였습니다."
        );
    }

//    /**
//     * 게시글 생성 API
//     */
//    @PostMapping()
//    public SingleResult<ArticleIdResponse> createArticle(
//            @RequestParam("article-id") Long articleId,
//            @RequestBody @Valid CreateArticleRequest request
//    ) {
//
//        return responseService.getSingleResult(
//                OK.value(),
//                "성공적으로 게시글이 생성되었습니다.",
//                articleService.createArticle(
//                        tempArticleId,
//                        request
//                )
//        );
//    }
}
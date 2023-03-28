package site.devtown.spadeworker.domain.article.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import site.devtown.spadeworker.domain.article.dto.SaveTempArticleRequest;
import site.devtown.spadeworker.domain.article.dto.SaveTempArticleResponse;
import site.devtown.spadeworker.domain.article.dto.TempArticleDto;
import site.devtown.spadeworker.domain.article.service.ArticleService;
import site.devtown.spadeworker.domain.file.dto.UploadContentImageRequest;
import site.devtown.spadeworker.domain.file.dto.UploadContentImageResponse;
import site.devtown.spadeworker.domain.file.validation.ContentImageValidate;
import site.devtown.spadeworker.global.response.CommonResult;
import site.devtown.spadeworker.global.response.ListResult;
import site.devtown.spadeworker.global.response.ResponseService;
import site.devtown.spadeworker.global.response.SingleResult;

import javax.validation.Valid;
import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RequestMapping("/api/articles")
@RestController
public class ArticleController {

    private final ArticleService articleService;
    private final ResponseService responseService;

    /**
     * 임시 게시글 저장 API
     */
    @PutMapping({"/temp-articles", "/temp-articles/{temp-article-id}"})
    public SingleResult<SaveTempArticleResponse> createTempArticle(
            @PathVariable("temp-article-id") Optional<Long> tempArticleId,
            @RequestBody @Valid SaveTempArticleRequest request
    ) {

        return responseService.getSingleResult(
                OK.value(),
                "성공적으로 임시 게시글이 저장되었습니다.",
                articleService.saveTempArticle(
                        tempArticleId,
                        request
                )
        );
    }

    /**
     * 게시글 본문 이미지 업로드 API
     */
    @PostMapping("/temp-articles/{temp-article-id}/article-content-images")
    public SingleResult<UploadContentImageResponse> saveArticleContentImage(
            @PathVariable("temp-article-id") Long tempArticleId,
            @ModelAttribute @Valid @ContentImageValidate UploadContentImageRequest request
    ) throws Exception {

        return responseService.getSingleResult(
                OK.value(),
                "게시글 컨텐츠 이미지가 성공적으로 업로드 되었습니다.",
                articleService.saveArticleContentImage(
                        tempArticleId,
                        request.image()
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
    @GetMapping("/temp-articles/{temp-article-id}")
    public SingleResult<TempArticleDto> getTempArticle(
            @PathVariable("temp-article-id") Long tempArticleId
    ) {

        return responseService.getSingleResult(
                OK.value(),
                "성공적으로 임시 게시글이 조회되었습니다.",
                articleService.getTempArticle(tempArticleId)
        );
    }

    /**
     * 임시 게시글 삭제 API
     */
    @DeleteMapping("/temp-articles/{temp-article-id}")
    public CommonResult deleteTempArticle(
            @PathVariable("temp-article-id") Long tempArticleId
    ) {

        articleService.deleteTempArticle(tempArticleId);

        return responseService.getSuccessResult(
                OK.value(),
                "성공적으로 임시 게시글이 삭제되었습니다."
        );
    }
}
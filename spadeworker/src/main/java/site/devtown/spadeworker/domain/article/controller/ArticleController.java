package site.devtown.spadeworker.domain.article.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import site.devtown.spadeworker.domain.article.dto.SaveTempArticleRequest;
import site.devtown.spadeworker.domain.article.dto.SaveTempArticleResponse;
import site.devtown.spadeworker.domain.article.service.ArticleService;
import site.devtown.spadeworker.domain.file.dto.UploadContentImageRequest;
import site.devtown.spadeworker.domain.file.dto.UploadContentImageResponse;
import site.devtown.spadeworker.domain.file.validation.ContentImageValidate;
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
    @PutMapping({"/temp-articles", "/temp-articles/{tempArticleId}"})
    public SingleResult<SaveTempArticleResponse> createTempArticle(
            @PathVariable Optional<Long> tempArticleId,
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
}
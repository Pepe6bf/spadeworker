package site.devtown.spadeworker.domain.article.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.devtown.spadeworker.domain.article.model.entity.Article;
import site.devtown.spadeworker.domain.article.model.entity.ArticleHashtag;
import site.devtown.spadeworker.domain.article.model.entity.Hashtag;
import site.devtown.spadeworker.domain.article.repository.ArticleHashtagRepository;
import site.devtown.spadeworker.domain.article.repository.HashtagRepository;
import site.devtown.spadeworker.global.exception.ResourceNotFoundException;

import java.util.Set;
import java.util.stream.Collectors;

import static site.devtown.spadeworker.domain.article.exception.ArticleExceptionCode.HASHTAG_NOT_FOUND;

@RequiredArgsConstructor
@Transactional
@Service
public class HashtagService {

    private final HashtagRepository hashtagRepository;
    private final ArticleHashtagRepository articleHashtagRepository;

    // 게시글 해시태그 저장
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

    // 게시글의 해시태그를 업데이트
    public void updateArticleHashtags(
            Set<String> hashtags,
            Article article
    ) {

        // 기존 해시태그 제거
        articleHashtagRepository.deleteAllByArticle(article);

        // 해시태그 업데이트
        saveArticleHashtags(hashtags, article);
    }

    // 새로 들어온 해시태그일 경우 생성
    private void createHashtag(
            String hashtag
    ) {
        if (!hashtagRepository.existsByTitle(hashtag)) {
            hashtagRepository.save(
                    Hashtag.of(hashtag)
            );
        }
    }

    // 게시글 해시태그 생성
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

    // 해시태그 엔티티 조회
    @Transactional(readOnly = true)
    protected Hashtag getHashtagEntity(
            String hashtag
    ) {
        return hashtagRepository.findByTitle(hashtag)
                .orElseThrow(() -> new ResourceNotFoundException(HASHTAG_NOT_FOUND));
    }

    /**
     * 게시글 해시태그 제목 전체 조회
     */
    public Set<String> getAllArticleHashtags(
            Article article
    ) {
        return articleHashtagRepository.findAllByArticle(article)
                .stream()
                .map(articleHashtag -> articleHashtag.getHashtag().getTitle())
                .collect(Collectors.toSet());
    }

    public void deleteArticleHashtags(
            Article article
    ) {
        articleHashtagRepository.deleteAllByArticle(article);
    }
}

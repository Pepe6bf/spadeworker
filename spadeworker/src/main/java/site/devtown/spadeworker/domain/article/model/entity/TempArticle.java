package site.devtown.spadeworker.domain.article.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.devtown.spadeworker.domain.article.model.constant.TempArticleType;
import site.devtown.spadeworker.domain.user.model.entity.User;
import site.devtown.spadeworker.global.config.audit.BaseEntity;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class TempArticle
        extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(length = 100, nullable = false)
    private TempArticleType articleType;

    @OneToOne(fetch = FetchType.LAZY)
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public TempArticle(
            String title,
            String content,
            TempArticleType articleType,
            Article article,
            User user
    ) {
        this.title = title;
        this.content = content;
        this.articleType = articleType;
        this.article = article;
        this.user = user;
    }

    public static TempArticle of(
            String title,
            String content,
            TempArticleType articleType,
            Article article,
            User user
    ) {
        return new TempArticle(
                title,
                content,
                articleType,
                article,
                user
        );
    }

    /**
     * 임시 게시글 수정
     */
    public void update(
            String title,
            String content
    ) {
        this.title = title;
        this.content = content;
    }
}
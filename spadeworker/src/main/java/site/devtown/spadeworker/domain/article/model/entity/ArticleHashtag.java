package site.devtown.spadeworker.domain.article.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.devtown.spadeworker.global.config.audit.BaseTimeEntity;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "article_hashtag",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "article-hashtag-unique",
                        columnNames = {
                                "hashtag_id",
                                "article_id"
                        }
                )
        }
)
@Entity
public class ArticleHashtag
        extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Hashtag hashtag;

    @ManyToOne(fetch = FetchType.LAZY)
    private Article article;

    private ArticleHashtag(
            Hashtag hashtag,
            Article article
    ) {
        this.hashtag = hashtag;
        this.article = article;
    }

    public static ArticleHashtag of(
            Hashtag hashtag,
            Article article
    ) {
        return new ArticleHashtag(
                hashtag,
                article
        );
    }
}

package site.devtown.spadeworker.domain.article.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.devtown.spadeworker.global.config.audit.BaseTimeEntity;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "temp_article_hashtag",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "temp-article-hashtag-unique",
                        columnNames = {
                                "title",
                                "temp_article_id"
                        }
                )
        }
)
@Entity
public class TempArticleHashtag
        extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    private TempArticle tempArticle;

    private TempArticleHashtag(
            String title,
            TempArticle tempArticle
    ) {
        this.title = title;
        this.tempArticle = tempArticle;
    }

    public static TempArticleHashtag of(
            String title,
            TempArticle tempArticle
    ) {
        return new TempArticleHashtag(title, tempArticle);
    }
}
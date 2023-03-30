package site.devtown.spadeworker.domain.article.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.devtown.spadeworker.global.config.audit.BaseTimeEntity;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ArticleContentImage
        extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000, nullable = false)
    private String imagePath;

    @ManyToOne(fetch = FetchType.LAZY)
    private Article article;

    private ArticleContentImage(
            String imagePath,
            Article article
    ) {
        this.imagePath = imagePath;
        this.article = article;
    }

    public static ArticleContentImage of(
            String imagePath,
            Article article
    ) {
        return new ArticleContentImage(imagePath, article);
    }
}
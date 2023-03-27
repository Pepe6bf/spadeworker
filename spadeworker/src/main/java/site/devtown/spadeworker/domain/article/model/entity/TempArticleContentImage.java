package site.devtown.spadeworker.domain.article.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.devtown.spadeworker.global.config.audit.BaseTimeEntity;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class TempArticleContentImage
        extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000, nullable = false)
    private String imagePath;

    @ManyToOne(fetch = FetchType.LAZY)
    private TempArticle tempArticle;

    private TempArticleContentImage(
            String imagePath,
            TempArticle tempArticle
    ) {
        this.imagePath = imagePath;
        this.tempArticle = tempArticle;
    }

    public static TempArticleContentImage of(
            String imagePath,
            TempArticle tempArticle
    ) {
        return new TempArticleContentImage(imagePath, tempArticle);
    }
}
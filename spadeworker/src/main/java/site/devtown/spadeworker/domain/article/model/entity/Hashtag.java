package site.devtown.spadeworker.domain.article.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.devtown.spadeworker.global.config.audit.BaseTimeEntity;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Hashtag
        extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    private Hashtag(
            String title
    ) {
        this.title = title;
    }

    public static Hashtag of(
            String title
    ) {
        return new Hashtag(title);
    }
}
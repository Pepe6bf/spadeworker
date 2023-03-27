package site.devtown.spadeworker.domain.article.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private TempArticle(
            String title,
            String content,
            User user
    ) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public static TempArticle of(
            String title,
            String content,
            User user
    ) {
        return new TempArticle(title, content, user);
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
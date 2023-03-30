package site.devtown.spadeworker.domain.article.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.devtown.spadeworker.domain.article.model.constant.ArticleStatus;
import site.devtown.spadeworker.domain.project.entity.Project;
import site.devtown.spadeworker.domain.user.model.entity.User;
import site.devtown.spadeworker.global.config.audit.BaseEntity;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Article
        extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(length = 1000, nullable = false)
    private String thumbnailImagePath;

    @Enumerated(EnumType.STRING)
    @Column(length = 100, nullable = false)
    private ArticleStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private Article(
            String title,
            String content,
            String thumbnailImagePath,
            ArticleStatus status,
            Project project,
            User user
    ) {
        this.title = title;
        this.content = content;
        this.thumbnailImagePath = thumbnailImagePath;
        this.status = status;
        this.project = project;
        this.user = user;
    }

    public static Article of(
            String title,
            String content,
            String thumbnailImagePath,
            ArticleStatus status,
            Project project,
            User user
    ) {
        return new Article(
                title,
                content,
                thumbnailImagePath,
                status,
                project,
                user
        );
    }
}
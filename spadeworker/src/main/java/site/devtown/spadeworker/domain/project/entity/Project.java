package site.devtown.spadeworker.domain.project.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.devtown.spadeworker.domain.user.model.entity.User;
import site.devtown.spadeworker.global.config.audit.BaseEntity;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Project extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String title;

    @Column(length = 1000, nullable = false)
    private String description;

    @Column(length = 1000, nullable = false)
    private String thumbnailImageUri;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private Project(
            String title,
            String description,
            String thumbnailImageUri,
            User user
    ) {
        this.title = title;
        this.description = description;
        this.thumbnailImageUri = thumbnailImageUri;
        this.user = user;
    }

    public static Project of(
            String title,
            String description,
            String thumbnailImageUri,
            User user
    ) {
        return new Project(
                title,
                description,
                thumbnailImageUri,
                user
        );
    }

    /**
     * Project Update
     */
    public void update(
            String title,
            String description,
            String thumbnailImageUri
    ) {
        this.title = title;
        this.description = description;
        this.thumbnailImageUri = thumbnailImageUri;
    }
}
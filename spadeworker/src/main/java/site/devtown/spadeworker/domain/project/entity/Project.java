package site.devtown.spadeworker.domain.project.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    private String thumbnailImageUri = "PROJECT_DEFAULT_URI";

    private Project(
            String title,
            String description,
            String thumbnailImageUri
    ) {
        this.title = title;
        this.description = description;
        this.thumbnailImageUri = thumbnailImageUri;
    }

    public static Project of(
            String title,
            String description,
            String thumbnailImageUri
    ) {
        return new Project(title, description, thumbnailImageUri);
    }
}
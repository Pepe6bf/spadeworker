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
    private String thumbnailUrl = "PROJECT_DEFAULT_URL";

    private Project(
            String title,
            String description,
            String thumbnailUrl
    ) {
        this.title = title;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
    }

    public static Project of(
            String title,
            String description,
            String thumbnailUrl
    ) {
        return new Project(title, description, thumbnailUrl);
    }
}
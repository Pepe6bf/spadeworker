package site.devtown.spadeworker.domain.project.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.devtown.spadeworker.domain.user.model.entity.User;
import site.devtown.spadeworker.global.config.audit.BaseTimeEntity;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "project_like",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "project-like-unique",
                        columnNames = {
                                "project_id",
                                "user_id"
                        }
                )
        }
)
@Entity
public class ProjectLike extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private ProjectLike(
            Project project,
            User user
    ) {
        this.project = project;
        this.user = user;
    }

    public static ProjectLike of(
            Project project,
            User user
    ) {
        return new ProjectLike(project, user);
    }
}
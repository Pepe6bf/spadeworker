package site.devtown.spadeworker.domain.project.entity;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.devtown.spadeworker.domain.user.model.entity.User;
import site.devtown.spadeworker.global.config.audit.BaseTimeEntity;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ProjectSubscribe extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    private User subscriber;

    private ProjectSubscribe(
            Project project,
            User subscriber
    ) {
        this.project = project;
        this.subscriber = subscriber;
    }

    public static ProjectSubscribe of(
            Project project,
            User subscriber
    ) {
        return new ProjectSubscribe(project, subscriber);
    }
}
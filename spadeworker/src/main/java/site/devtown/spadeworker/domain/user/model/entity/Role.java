package site.devtown.spadeworker.domain.user.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.devtown.spadeworker.domain.user.model.constant.UserRoleType;
import site.devtown.spadeworker.global.config.audit.BaseTimeEntity;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Role extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 100, nullable = false, unique = true)
    private UserRoleType roleType;
}

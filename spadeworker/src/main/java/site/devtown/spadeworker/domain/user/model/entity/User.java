package site.devtown.spadeworker.domain.user.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.devtown.spadeworker.domain.user.model.constant.AuthProviderType;
import site.devtown.spadeworker.domain.user.model.constant.UserStatus;
import site.devtown.spadeworker.global.config.audit.BaseTimeEntity;

import javax.persistence.*;

import static site.devtown.spadeworker.domain.user.model.constant.UserStatus.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false, unique = true, updatable = false)
    private String personalId;

    @Column(length = 1000, nullable = false)
    private String password;

    @Column(length = 100, nullable = false)
    private String nickname;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false, updatable = false)
    private AuthProviderType providerType;

    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    private UserStatus status = ACTIVE;

    @Column(length = 1000, nullable = false)
    private String profileImagePath;

    @Column(length = 1000)
    private String introduce = "";

    private User(
            String personalId,
            String password,
            String nickname,
            String email,
            AuthProviderType providerType,
            String profileImagePath
    ) {
        this.personalId = personalId;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.providerType = providerType;
        this.profileImagePath = profileImagePath;
    }

    public static User of(
            String personalId,
            String password,
            String nickname,
            String email,
            AuthProviderType providerType,
            String profileImagePath
    ) {
        return new User(
                personalId,
                password,
                nickname,
                email,
                providerType,
                profileImagePath
        );
    }
}
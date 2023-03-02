package site.devtown.spadeworker.domain.user.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.devtown.spadeworker.domain.user.model.constant.AuthProviderType;
import site.devtown.spadeworker.domain.user.model.constant.UserStatus;
import site.devtown.spadeworker.global.config.audit.BaseTimeEntity;

import javax.persistence.*;

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
    private UserStatus status;

    @Column(length = 1000, nullable = false)
    private String profileImageUri =
            "/Users/kmo/toy-project/spadeworker-project/local-storage/images"
                    + "/user-profile"
                    + "/user-profile_default.jpg";

    @Column(length = 1000)
    private String introduce = null;

    private User(
            String personalId,
            String password,
            String nickname,
            String email,
            AuthProviderType providerType,
            UserStatus status
    ) {
        this.personalId = personalId;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.providerType = providerType;
        this.status = status;
    }

    public static User of(
            String personalId,
            String password,
            String nickname,
            String email,
            AuthProviderType providerType,
            UserStatus status
    ) {
        return new User(
                personalId,
                password,
                nickname,
                email,
                providerType,
                status
        );
    }
}
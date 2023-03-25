package site.devtown.spadeworker.global.util.fixture;

import site.devtown.spadeworker.domain.user.model.constant.AuthProviderType;
import site.devtown.spadeworker.domain.user.model.entity.User;

import static site.devtown.spadeworker.domain.user.model.constant.AuthProviderType.GOOGLE;

public class UserFixture {

    public static String personalId = "tester1727";
    public static String password = "testerPw1727";
    public static String nickname = "tester";
    public static String email = "tester@email.com";
    public static AuthProviderType providerType = GOOGLE;

    public static User getUserEntity() {
        return getUserEntity(
                personalId,
                password,
                nickname,
                email,
                providerType
        );
    }

    public static User getUserEntity(
            String personalId,
            String password,
            String nickname,
            String email,
            AuthProviderType providerType
    ) {
        return User.of(
                personalId,
                password,
                nickname,
                email,
                providerType,
                "default-user-profile-image-path"
        );
    }
}
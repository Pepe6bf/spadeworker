package site.devtown.spadeworker.domain.auth.model.info;

import site.devtown.spadeworker.domain.user.model.constant.AuthProviderType;

import java.util.Map;


public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(
            AuthProviderType providerType,
            Map<String, Object> attributes
    ) {
        return switch (providerType) {
            case GOOGLE -> new GoogleOAuth2UserInfo(attributes);
            case KAKAO -> new KakaoOAuth2UserInfo(attributes);
            default -> throw new IllegalArgumentException("Invalid Provider Type.");
        };
    }
}

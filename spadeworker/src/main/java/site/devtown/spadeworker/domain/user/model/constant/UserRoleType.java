package site.devtown.spadeworker.domain.user.model.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum UserRoleType {

    GUEST("ROLE_GUEST", "게스트 권한"),
    USER("ROLE_USER", "일반 사용자 권한"),
    ADMIN("ROLE_ADMIN", "관리자 권한");

    private final String code;
    private final String title;

    public static UserRoleType of(String code) {
        return Arrays.stream(UserRoleType.values())
                .filter(r -> r.getCode().equals(code))
                .findAny()
                .orElse(GUEST);
    }
}

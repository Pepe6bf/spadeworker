package site.devtown.spadeworker.domain.user.model.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserStatus {
    ACTIVE("활성화 중인 사용자"),
    BLOCK("차단된 사용"),
    DORMANT("휴먼 상태인 사용"),
    DELETE("삭제된 사용자");

    private final String description;
}

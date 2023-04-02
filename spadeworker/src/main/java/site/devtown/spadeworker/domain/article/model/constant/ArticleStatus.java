package site.devtown.spadeworker.domain.article.model.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ArticleStatus {
    OPEN("open"),
    HIDE("hide"),
    TEMP("temp"),
    DELETED("deleted");

    private final String value;

    @JsonCreator
    public static ArticleStatus from(String value) {
        for (ArticleStatus status : ArticleStatus.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        return ArticleStatus.OPEN;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
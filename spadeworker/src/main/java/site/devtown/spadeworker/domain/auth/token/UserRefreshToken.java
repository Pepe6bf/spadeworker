package site.devtown.spadeworker.domain.auth.token;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.devtown.spadeworker.global.config.audit.BaseTimeEntity;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserRefreshToken extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false, updatable = false)
    private String personalId;

    @Column(length = 500, nullable = false, updatable = false)
    private String tokenValue;

    private UserRefreshToken(
            String personalId,
            String tokenValue
    ) {
        this.personalId = personalId;
        this.tokenValue = tokenValue;
    }

    public static UserRefreshToken of(
            String personalId,
            String tokenValue
    ) {
        return new UserRefreshToken(personalId, tokenValue);
    }
}

package site.devtown.spadeworker.domain.auth.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import site.devtown.spadeworker.global.exception.ExceptionCode;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum AuthExceptionCode
        implements ExceptionCode {

    /**
     * 확인 불명 오류 코드
     * 0 ~ 99
     */
    AUTHENTICATION_CLIENT_EXCEPTION(UNAUTHORIZED, "A000", "사용자 인증에 실패하였습니다."),
    AUTHENTICATION_SERVICE_EXCEPTION(INTERNAL_SERVER_ERROR, "A001", "인증 로직이 실패하였습니다."),

    /**
     * JWT
     * 100 ~ 199
     */
    INVALID_TOKEN(UNAUTHORIZED, "A100", "유효하지 않은 토큰 입니다."),
    FAILED_GENERATE_TOKEN(INTERNAL_SERVER_ERROR, "A101", "토큰 생성에 실패하였습니다."),
    EXPIRED_TOKEN(UNAUTHORIZED, "A102", "만료된 토큰 입니다."),
    NOT_EXPIRED_TOKEN(UNAUTHORIZED, "A103", "만료되지 않은 토큰 입니다."),
    INVALID_REFRESH_TOKEN(BAD_REQUEST, "A104", "유효하지 않은 refresh token 입니다."),
    NO_TOKEN(BAD_REQUEST, "A105", "요청에 토큰이 존재하지 않습니다."),

    INVALID_TOKEN_SIGNATURE(UNAUTHORIZED, "ACC-003", "유효하지 않은 토큰 시그니쳐입니다."),
    UNSUPPORTED_TOKEN(UNAUTHORIZED, "ACC-006", "지원하지 않는 토큰입니다."),
    INVALID_TOKEN_COMPACT(INTERNAL_SERVER_ERROR, "ACC-007", "유효하지 않는 토큰 생성입니다."),
    AUTHENTICATION_ERROR(UNAUTHORIZED, "ACC-019", "Authentication exception."),
    INTERNAL_AUTHENTICATION_SERVICE_EXCEPTION(INTERNAL_SERVER_ERROR, "ACC-020", "Internal authentication service exception.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
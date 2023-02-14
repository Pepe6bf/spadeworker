package site.devtown.spadeworker.global.response;

import lombok.Getter;

/**
 * 단건 결과값만 반환하는 응답
 */
@Getter
public class SingleResult<T> extends CommonResult {

    private T data;

    public SingleResult(Boolean isSuccess, Integer status, String message, T data) {
        super(isSuccess, status, message);
        this.data = data;
    }
}

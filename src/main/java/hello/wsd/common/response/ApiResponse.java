package hello.wsd.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
public class ApiResponse <T>{

    private static final Boolean SUCCESS = true;
    private static final Boolean FAIL = false;

    private Boolean isSuccess;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    // 성공 응답 생성자
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(SUCCESS, data);
    }

    // 실패 응답 생성자
    public static <T> ApiResponse<T> fail(T data) {
        return new ApiResponse<>(FAIL, data);
    }

    private ApiResponse(Boolean isSuccess, T data) {
        this.isSuccess = isSuccess;
        this.data = data;
    }
}

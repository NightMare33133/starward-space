package top.starward.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 🌌 统一 API 响应包装体 (标准化 RESTful JSON 返回格式)
 *
 * @param <T> 数据泛型
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 业务状态码 (200 表示成功)
     */
    private int code;

    /**
     * 提示消息
     */
    private String message;

    /**
     * 业务负载数据
     */
    private T data;

    /**
     * 响应时间戳 (毫秒)
     */
    @Builder.Default
    private long timestamp = System.currentTimeMillis();

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> success(T data) {
        return Result.<T>builder()
                .code(ResultCode.SUCCESS.getCode())
                .message(ResultCode.SUCCESS.getMessage())
                .data(data)
                .build();
    }

    public static <T> Result<T> success(String message, T data) {
        return Result.<T>builder()
                .code(ResultCode.SUCCESS.getCode())
                .message(message)
                .data(data)
                .build();
    }

    public static <T> Result<T> error(String message) {
        return Result.<T>builder()
                .code(ResultCode.INTERNAL_SERVER_ERROR.getCode())
                .message(message)
                .data(null)
                .build();
    }

    public static <T> Result<T> error(ResultCode resultCode) {
        return Result.<T>builder()
                .code(resultCode.getCode())
                .message(resultCode.getMessage())
                .data(null)
                .build();
    }

    public static <T> Result<T> error(int code, String message) {
        return Result.<T>builder()
                .code(code)
                .message(message)
                .data(null)
                .build();
    }
}

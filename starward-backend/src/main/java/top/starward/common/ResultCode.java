package top.starward.common;

import lombok.Getter;

/**
 * 🌌 统一响应业务状态码枚举
 */
@Getter
public enum ResultCode {

    SUCCESS(200, "操作成功"),
    BAD_REQUEST(400, "请求参数异常"),
    UNAUTHORIZED(401, "账号未登录或凭据已过期"),
    FORBIDDEN(403, "没有操作权限"),
    NOT_FOUND(404, "目标资源不存在"),
    METHOD_NOT_ALLOWED(405, "不支持当前请求方法"),
    INTERNAL_SERVER_ERROR(500, "系统内部繁忙，请稍后重试");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}

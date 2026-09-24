package top.starward.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import top.starward.common.Result;
import top.starward.common.ResultCode;

import java.util.stream.Collectors;

/**
 * 🌌 全局统一异常处理器 (Global Exception Handler)
 * 作用：拦截所有未捕获异常，包装为标准 Result<T> 返回，绝不向客户端暴露内部堆栈与敏感信息
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 1. 拦截已知自定义业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        log.warn("业务规则校验未通过: code={}, message={}", e.getCode(), e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }

    /**
     * 2. 拦截 Spring Validation 参数校验异常 (@NotBlank, @NotNull, @Size 等)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidationException(MethodArgumentNotValidException e) {
        String errorMsg = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        log.warn("入参校验失败: {}", errorMsg);
        return Result.error(ResultCode.BAD_REQUEST.getCode(), errorMsg);
    }

    /**
     * 3. 拦截参数非法异常 (IllegalArgumentException)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("非法参数: {}", e.getMessage());
        return Result.error(ResultCode.BAD_REQUEST.getCode(), e.getMessage());
    }

    /**
     * 4. 拦截 404 资源未找到异常 (Spring Boot 3 NoResourceFoundException)
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public Result<Void> handleNotFoundException(NoResourceFoundException e) {
        return Result.error(ResultCode.NOT_FOUND.getCode(), "请求的星际路径不存在");
    }

    /**
     * 5. 兜底防御：拦截一切未知内部异常
     * 严格脱敏：记录完整日志，但只给外部返回模糊且安全的友好提示，绝不泄露表名、行号与数据库结构！
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleGeneralException(Exception e) {
        log.error("系统发生未捕获异常: ", e);
        return Result.error(ResultCode.INTERNAL_SERVER_ERROR.getCode(), "星际网络微澜，系统繁忙，请稍后重试");
    }
}

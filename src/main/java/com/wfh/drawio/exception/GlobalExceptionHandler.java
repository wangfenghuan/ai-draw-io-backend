package com.wfh.drawio.exception;


import com.wfh.drawio.common.BaseResponse;
import com.wfh.drawio.common.ErrorCode;
import com.wfh.drawio.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author wangfenghuan
 * @from wangfenghuan
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("BusinessException", e);
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统错误");
    }

    @ExceptionHandler(AuthenticationException.class) // import org.springframework.security.core.AuthenticationException
    public BaseResponse<?> handleAuthenticationException(AuthenticationException e) {
        // 或者是 ErrorCode.PARAMS_ERROR，看你想报什么错
        return ResultUtils.error(ErrorCode.OPERATION_ERROR, "账号或密码错误");
    }
}

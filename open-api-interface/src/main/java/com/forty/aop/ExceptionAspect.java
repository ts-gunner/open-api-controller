package com.forty.aop;

import com.forty.common.BaseResponse;
import com.forty.common.CodeStatus;
import com.forty.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.MessageFormat;

/**
 * 全局异常处理
 */
@Slf4j
@ControllerAdvice
public class ExceptionAspect {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public BaseResponse<Object> exceptionHandler(Exception e) {
        if (e instanceof BusinessException businessException) {
            return new BaseResponse<>(businessException.getCode(), businessException.getMessage());
        }
        log.error(MessageFormat.format("【system error】: {0}", e.getMessage()));
        return new BaseResponse<>(CodeStatus.SYSTEM_ERROR);
    }
}
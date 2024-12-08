package com.forty.wall.exception;
import cn.hutool.core.bean.BeanUtil;
import com.forty.common.BaseResponse;
import com.forty.common.CodeStatus;
import com.forty.exception.BusinessException;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.util.Map;

public class GlobalExceptionHandler extends DefaultErrorWebExceptionHandler {

    public GlobalExceptionHandler(ErrorAttributes errorAttributes, WebProperties.Resources resources, ErrorProperties errorProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resources, errorProperties, applicationContext);
    }

    /**
     * 指定使用json格式进行响应
     */
    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        //参数1：匹配所有的请求(所有出现异常的请求)  参数2：指定自己设置响应(默认转为json)
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }
    /**
     * 2、提供异常数据  : 响应报文数据
     *
     */
    @Override
    protected Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {

        Throwable error = getError(request);//获取请求中的异常信息
        BaseResponse<Object> response;
        if (error instanceof BusinessException businessException) {
            response = new BaseResponse<>(businessException.getCode(), businessException.getMessage());
        }else {
            response = new BaseResponse<>(
                    CodeStatus.SYSTEM_ERROR.getCode(),
                    MessageFormat.format("【system error】: {0}", error.getMessage())
            );
        }
        return BeanUtil.beanToMap(response);
    }

    /**
     * 3、提供响应报文的状态码
     */
    @Override
    protected int getHttpStatus(Map<String, Object> errorAttributes) {
        // errorAttributes代表上面方法中封装的响应数据
        return HttpStatus.OK.value();
    }

    /**
     * 重写renderErrorResponse
     */
    @Override
    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Map<String, Object> errorAttributes = this.getErrorAttributes(request, ErrorAttributeOptions.defaults());
        int status = this.getHttpStatus(errorAttributes);
        return ServerResponse.status(status).contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(errorAttributes));
    }
}

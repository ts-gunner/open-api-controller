package com.forty.interceptor;

import com.forty.common.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SignInterceptor implements HandlerInterceptor {

    public boolean preHandle(HttpServletRequest request) {
        String sign =request.getHeader("sign");
        String nonce =request.getHeader("nonce");
        String timestamp =request.getHeader("timestamp");
        String body =request.getHeader("body");
        String secretId =request.getHeader("secretId");
        return true;
    }
}

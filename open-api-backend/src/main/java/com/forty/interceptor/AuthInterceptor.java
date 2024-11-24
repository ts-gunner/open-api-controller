package com.forty.interceptor;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forty.common.CodeStatus;
import com.forty.config.Settings;
import com.forty.exception.BusinessException;
import com.forty.model.entity.TokenData;
import com.forty.utils.JWTUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Resource
    Settings settings;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if (token == null) throw new BusinessException(CodeStatus.NO_AUTH);
        try {
            Map<String, Object> map = JWTUtils.decrypt(token, settings.getSecretKey());
            TokenData data = JSON.parseObject(JSON.toJSONString(map), TokenData.class);
            request.setAttribute("tokenData", data);
        }catch (Exception e){
            throw new BusinessException(CodeStatus.NO_AUTH, "身份验证失败: " + e.getMessage());
        }
        return true;
    }
}

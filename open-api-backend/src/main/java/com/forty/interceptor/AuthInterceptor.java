package com.forty.interceptor;

import com.alibaba.fastjson2.JSON;
import com.forty.annotation.RoleCheck;
import com.forty.config.Settings;
import com.forty.model.entity.TokenData;
import com.forty.utils.JWTUtils;
import com.forty.common.CodeStatus;
import com.forty.exception.BusinessException;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;
import java.util.Map;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Resource
    Settings settings;


    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if (token == null) throw new BusinessException(CodeStatus.NO_AUTH);
        TokenData data;
        try {
            Map<String, Object> map = JWTUtils.decrypt(token, settings.getSecretKey());
            data = JSON.parseObject(JSON.toJSONString(map), TokenData.class);
            if (data == null) throw new BusinessException(CodeStatus.NO_AUTH);
        }catch (Exception e){
            throw new BusinessException(CodeStatus.NO_AUTH, "身份验证失败: " + e.getMessage());
        }

        request.setAttribute("tokenData", data);
        // 校验用户是否满足所有角色
        verifyUserRequireRole(data.getRoles(), handler);
        // 校验用户是否满足其中一个角色即可
        verifyUserHasRole(data.getRoles(), handler);
        return true;
    }

    public void verifyUserRequireRole(List<String> userRoleList, Object handler) {
        if  (handler instanceof HandlerMethod handlerMethod) {
            RoleCheck roleCheck = handlerMethod.getMethodAnnotation(RoleCheck.class);
            if (roleCheck != null) {
                String[] requiredRoles = roleCheck.requiredRoles();
                for (String requiredRole : requiredRoles) {
                    if (!userRoleList.contains(requiredRole)) throw new BusinessException(CodeStatus.AUTH_ROLE_FAILED);
                }
            }
        }
    }

    public void verifyUserHasRole(List<String> userRoleList, Object handler) {
        if  (handler instanceof HandlerMethod handlerMethod) {
            RoleCheck roleCheck = handlerMethod.getMethodAnnotation(RoleCheck.class);
            if (roleCheck != null) {
                String[] hasRoles = roleCheck.hasRoles();
                boolean isHasRole = false;
                for (String hasRole : hasRoles) {
                    if (userRoleList.contains(hasRole)) {
                        isHasRole = true;
                        break;
                    }
                }
                if (!isHasRole) throw new BusinessException(CodeStatus.AUTH_ROLE_FAILED);
            }
        }
    }
}

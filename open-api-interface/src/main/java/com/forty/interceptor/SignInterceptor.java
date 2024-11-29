package com.forty.interceptor;

import com.forty.common.CodeStatus;
import com.forty.exception.BusinessException;
import com.forty.model.SecretInfo;
import com.forty.service.SecretService;
import com.forty.utils.EncryptUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SignInterceptor implements HandlerInterceptor {

    @Resource
    SecretService secretService;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String sign =request.getHeader("sign");
        String nonce =request.getHeader("nonce");
        String timestamp =request.getHeader("timestamp");
        String body =request.getHeader("body");
        String secretId =request.getHeader("secretId");
        SecretInfo secretInfo = secretService.getSecretInfo(secretId);
        // 校验该secretId是否有效
        if (secretInfo == null) throw new BusinessException(CodeStatus.SIGN_AUTH_FAILED);

//        // 对比sign是否相同
//        String encryptSign = EncryptUtils.generateEncryptString(body + "." + secretInfo.getSecretKey());
//        if (!encryptSign.equals(sign)) throw new BusinessException(CodeStatus.SIGN_AUTH_FAILED);

        // 查看nonce是否在redis中出现， 将nonce记录到redis中

        System.out.println(secretInfo);
        System.out.println(request.getHeaderNames());
        return true;
    }
}

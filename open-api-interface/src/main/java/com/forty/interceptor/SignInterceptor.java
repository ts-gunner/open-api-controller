package com.forty.interceptor;

import cn.hutool.core.convert.Convert;
import com.forty.common.CodeStatus;
import com.forty.exception.BusinessException;
import com.forty.model.SecretInfo;
import com.forty.service.SecretService;
import com.forty.utils.EncryptUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SignInterceptor implements HandlerInterceptor {

    @Resource
    SecretService secretService;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//        String sign =request.getHeader("sign");
//        String nonce =request.getHeader("nonce");
//        String timestamp =request.getHeader("timestamp");
//        String body =request.getHeader("body");
//        String secretId =request.getHeader("secretId");
//        SecretInfo secretInfo = secretService.getSecretInfo(secretId);
//        String nonceRedisKey = "openapi:nonce:" + secretId;
//        // 校验该secretId是否有效
//        if (secretInfo == null) throw new BusinessException(CodeStatus.SIGN_AUTH_FAILED);
//
//        // 对比sign是否相同
//        String encryptSign = EncryptUtils.generateEncryptString(Convert.unicodeToStr(body) + "." + secretInfo.getSecretKey());
//        if (!encryptSign.equals(sign)) throw new BusinessException(CodeStatus.SIGN_AUTH_FAILED);
//
//        SetOperations<String, Object> set = redisTemplate.opsForSet();
//        // 查看nonce是否在redis中出现， 将nonce记录到redis中
//        if (Boolean.TRUE.equals(set.isMember(nonceRedisKey, nonce))) throw new BusinessException(CodeStatus.SIGN_AUTH_FAILED);
//        // 将nonce保存到redis中
//        set.add(nonceRedisKey, nonce);
//        // 校验timestamp是否在允许范围内(暂不实现)
//
        return true;
    }
}

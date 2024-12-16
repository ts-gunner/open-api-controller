package com.forty.wall.filters;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.forty.common.BaseResponse;
import com.forty.common.CodeStatus;
import com.forty.exception.BusinessException;
import com.forty.model.entity.SecretInfo;
import com.forty.remote.InterfaceInnerService;
import com.forty.remote.SecretInnerService;
import com.forty.utils.EncryptUtils;
import com.forty.utils.PathUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;


@Component
public class OpenApiHandlerFilter implements GlobalFilter, Ordered {

    @DubboReference
    SecretInnerService secretInnerService;

    @DubboReference
    InterfaceInnerService interfaceInnerService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();
        boolean matchPath = PathUtils.isMatchPath(List.of("/public_api/**"), path);
        if (!matchPath) return chain.filter(exchange);
        // 校验签名是否合法
        SecretInfo secretInfo = signVerification(exchange);

        // 发布与在线调用的时候不需要校验且不需要统计调用次数
        // todo: 当前跳过校验的逻辑写到SDK，有风险，不应该写在SDK中
        if (Objects.equals(request.getHeaders().getFirst("test-api-skip-authorization"), "true")){
            return chain.filter(exchange);
        }

        // 校验该用户能否调用该接口（判断接口是否存在，判断接口状态是否开启状态）
        BaseResponse<Integer> verifyResult = interfaceInnerService.verifyInterfaceAvailable(secretInfo.getUserId(), request.getURI().toString());
        if (verifyResult.getCode() != CodeStatus.SUCCESS.getCode()) {
            throw new BusinessException(CodeStatus.INTERFACE_CALL_FAILED, verifyResult.getMsg());
        }

        ServerHttpResponse originalResponse = exchange.getResponse();
        DataBufferFactory bufferFactory = originalResponse.bufferFactory();
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    StringBuilder stringBuilder = new StringBuilder();

                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
                    Mono<Void> voidMono = super.writeWith(fluxBody.map(dataBuffer -> {
                        // probably should reuse buffers
                        byte[] content = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(content);
                        //释放掉内存
                        DataBufferUtils.release(dataBuffer);
                        String jsonStr = new String(content, Charset.forName("UTF-8"));
                        stringBuilder.append(jsonStr);

                        //TODO，s就是response的值，想修改、查看就随意而为了
                        byte[] uppedContent = StringUtils.getBytes(jsonStr, StandardCharsets.UTF_8);
                        return bufferFactory.wrap(uppedContent);
                    }));

                    BaseResponse responseData;
                    try {
                        responseData = JSONUtil.toBean(stringBuilder.toString(), BaseResponse.class);
                    }catch (Exception e){
                        responseData = new BaseResponse(stringBuilder.toString());
                    }
                    if (this.getStatusCode().value() == HttpStatus.OK.value() && responseData.getCode() == HttpStatus.OK.value()){
                        // 调用统计次数 + 1
                        boolean callResult = interfaceInnerService.invokeInterfaceCount(secretInfo.getUserId(), verifyResult.getData());
                        if (!callResult) throw new BusinessException(CodeStatus.INTERFACE_CALL_FAILED, "接口调用成功， 统计错误");
                    }

                    return voidMono;

                }
                // if body is not a flux. never got there.

                return super.writeWith(body);
            }
        };

        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }

    @Override
    public int getOrder() {
        return -1;
    }

    /**
     * 签名校验
     */
    private SecretInfo signVerification(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        String sign = headers.getFirst("sign");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String body = headers.getFirst("body");
        String secretId = headers.getFirst("secretId");
        SecretInfo secretInfo = secretInnerService.getSecretInfo(secretId);
        String nonceRedisKey = "openapi:nonce:" + secretId;
        // 校验该secretId是否有效
        if (secretInfo == null) throw new BusinessException(CodeStatus.SIGN_AUTH_FAILED, "用户需要申请一个secretKey");

        // 对比sign是否相同
        String encryptSign = EncryptUtils.generateEncryptString(Convert.unicodeToStr(body) + "." + secretInfo.getSecretKey());
        if (!encryptSign.equals(sign)) throw new BusinessException(CodeStatus.SIGN_AUTH_FAILED);

//        SetOperations<String, Object> set = redisTemplate.opsForSet();
//        // 查看nonce是否在redis中出现， 将nonce记录到redis中
//        if (Boolean.TRUE.equals(set.isMember(nonceRedisKey, nonce))) throw new BusinessException(CodeStatus.SIGN_AUTH_FAILED);
//        // 将nonce保存到redis中
//        set.add(nonceRedisKey, nonce);
        // 校验timestamp是否在允许范围内(暂不实现)
        return secretInfo;
    }
}

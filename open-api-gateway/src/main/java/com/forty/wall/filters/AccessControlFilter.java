package com.forty.wall.filters;


import cn.hutool.core.net.Ipv4Util;
import com.forty.wall.utils.HostUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 访问控制
 * 1. 黑白名单
 */
@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "openapi.gateway")
public class AccessControlFilter implements GlobalFilter, Ordered {

    private List<String> whiteList;

    private final List<String> DEFAULT_IP_WHITE_LIST = Arrays.asList(
            "127.0.0.1", "0:0:0:0:0:0:0:1"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        // 访问控制 - 黑白名单，不在白名单中的直接返回403，不再返回异常的json数据
        String sourceIP = Objects.requireNonNull(request.getLocalAddress()).getHostString();
        String realIP = HostUtils.getIpAddress(request);
        ServerHttpResponse response = exchange.getResponse();
        List<String> IP_WHITE_LIST = whiteList == null ? DEFAULT_IP_WHITE_LIST :
                Stream.concat(whiteList.stream(), DEFAULT_IP_WHITE_LIST.stream()).toList();
//        if (!IP_WHITE_LIST.contains(sourceIP) && !IP_WHITE_LIST.contains(realIP)) {
//            response.setStatusCode(HttpStatus.FORBIDDEN);
//            return response.setComplete();
//        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}

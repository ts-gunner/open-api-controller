package com.forty.wall.filters;


import com.forty.utils.IpAddressUtils;
import com.forty.wall.config.NacosConfiguration;
import com.forty.wall.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
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
@Component
public class AccessControlFilter implements GlobalFilter, Ordered {

    @Autowired
    NacosConfiguration nacosConfiguration;

    private final List<String> DEFAULT_IP_WHITE_LIST = Arrays.asList(
            "127.0.0.1", "0:0:0:0:0:0:0:1"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        // 访问控制 - 黑白名单，不在白名单中的直接返回403，不再返回异常的json数据
        String sourceIP = Objects.requireNonNull(request.getLocalAddress()).getHostString();
        String realIP = IpUtils.getIpAddress(request);
        ServerHttpResponse response = exchange.getResponse();
        List<String> whiteList = nacosConfiguration.getWhiteList();
        List<String> IP_WHITE_LIST = whiteList == null ? DEFAULT_IP_WHITE_LIST :
                Stream.concat(whiteList.stream(), DEFAULT_IP_WHITE_LIST.stream()).toList();
        StringBuilder accessLog = new StringBuilder();
        accessLog.append("\n\n========================= access control Start =========================\n");
        accessLog.append("real IP is " + realIP + ";\n");
        accessLog.append("source IP is " + sourceIP + ";\n");
        accessLog.append("white list is " + IP_WHITE_LIST + ";\n");
        accessLog.append("=========================  access control End  =========================\n");
        log.info(accessLog.toString());
        if (!judgeIpInWhite(sourceIP, IP_WHITE_LIST) && !judgeIpInWhite(realIP, IP_WHITE_LIST)) {
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }

    /**
     * 判断IP是否在白名单中
     */
    private boolean judgeIpInWhite(String ip, List<String> whiteList) {
        for (String whiteIp : whiteList) {
            boolean validIP = IpAddressUtils.isValidIP(whiteIp);
            boolean validSubnet = IpAddressUtils.isValidSubnet(whiteIp);
            if (validIP && validSubnet) {
                if (IpAddressUtils.isIpAddressInSubnet(ip, whiteIp)) return true;
            } else if (validIP) {
                if (whiteIp.equals(ip)) return true;
            }
        }
        return false;
    }
}

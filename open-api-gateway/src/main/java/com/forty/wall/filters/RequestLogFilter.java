package com.forty.wall.filters;


import cn.hutool.core.text.AntPathMatcher;
import cn.hutool.core.util.StrUtil;
import com.forty.utils.PathUtils;
import com.forty.wall.constant.CommonConstant;
import com.forty.wall.utils.HostUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.text.MessageFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 请求日志
 * 1. 输出请求路径
 * 2. 请求的真实IP
 */

@Slf4j
@Configuration
@ConditionalOnProperty(value = "log.request.enabled", havingValue = "true", matchIfMissing = true)
public class RequestLogFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        // 请求路径
        String path = request.getPath().value();

        // 不对某些路径进行过滤
        boolean matchPath = PathUtils.isMatchPath(CommonConstant.DEFAULT_EXCLUDE_PATTERNS, path);
        if (matchPath) return chain.filter(exchange);
        // 请求地址
        String ipAddress = HostUtils.getIpAddress(request);
        // 请求时间
        Instant requestTime = Instant.now();
        ZonedDateTime zonedDateTime = requestTime.atZone(ZoneId.of("Asia/Shanghai"));
        String requestFormatTime = zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        StringBuilder requestLog = new StringBuilder(255);
        requestLog.append("\n\n========================= Gateway Request Start =========================\n");
        requestLog.append(MessageFormat.format("request time: {0}\n", requestFormatTime));
        requestLog.append(MessageFormat.format("request path: {0}\n", path));
        requestLog.append(MessageFormat.format("request IP: {0}\n", ipAddress));

        // 打印请求头
        HttpHeaders headers = request.getHeaders();
        headers.forEach((headerName, headerValue) -> {
            requestLog.append(MessageFormat.format(
                    "[request header] -- {0}: {1}\n",
                    headerName,
                    StrUtil.join(",",headerValue.toArray())
                    ));
        });

        requestLog.append("=========================  Gateway Request End  =========================\n");
        log.info(requestLog.toString());
        return chain.filter(exchange);
    }


    @Override
    public int getOrder() {
        return 0;
    }
}

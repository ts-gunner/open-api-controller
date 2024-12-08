package com.forty.wall.filters;

import cn.hutool.core.util.StrUtil;
import com.forty.utils.PathUtils;
import com.forty.wall.constant.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 响应日志
 */
@Slf4j
@Component
public class ResponseLogFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        // 打印请求路径
        String path = request.getPath().pathWithinApplication().value();
        // 不对某些路径进行过滤
        boolean matchPath = PathUtils.isMatchPath(CommonConstant.DEFAULT_EXCLUDE_PATTERNS, path);
        if (matchPath) return chain.filter(exchange);
        return chain.filter(exchange).then(
                Mono.fromRunnable(() -> {
                    MultiValueMap<String, String> queryParams = request.getQueryParams();
                    String requestUrl = UriComponentsBuilder.fromPath(path).queryParams(queryParams).build().toUriString();
                    Instant requestTime = Instant.now();
                    ZonedDateTime zonedDateTime = requestTime.atZone(ZoneId.of("Asia/Shanghai"));
                    String responseFormatTime = zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    // 构建成一条长日志
                    StringBuilder responseLog = new StringBuilder(200);
                    // 日志参数
                    List<Object> responseArgs = new ArrayList<>();
                    responseLog.append("\n\n========================= Gateway Response Start  =========================\n");
                    ServerHttpResponse response = exchange.getResponse();
                    // 状态码个path占位符: 200 get: /xxx/xxx/xxx?a=b
                    responseLog.append("{} --- {} {}: {}\n");
                    // 参数
                    String requestMethod = request.getMethod().name();
                    responseArgs.add(responseFormatTime);
                    responseArgs.add(response.getStatusCode().value());
                    responseArgs.add(requestMethod);
                    responseArgs.add(requestUrl);

                    // 打印请求头
                    HttpHeaders headers = response.getHeaders();
                    headers.forEach((headerName, headerValue) -> {
                        responseLog.append("[response header] -- {}: {}\n");
                        responseArgs.add(headerName);
                        responseArgs.add(StrUtil.join(",",headerValue.toArray()));
                    });

                    responseLog.append("=========================  Gateway Response End  =========================\n");
                    // 打印执行时间
                    log.info(responseLog.toString(), responseArgs.toArray());
                })
        );

    }

    @Override
    public int getOrder() {
        return 0;
    }
}
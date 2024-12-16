package com.forty.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * 实现热更新
 */
@Data
@RefreshScope
@Configuration
public class NacosConfig {

    @Value("${forty.client.secretId}")
    private String secretId;

    @Value("${forty.client.secretKey}")
    private String secretKey;

    @Value("${forty.client.openApiUrl:#{null}}")
    private String openApiUrl;

}

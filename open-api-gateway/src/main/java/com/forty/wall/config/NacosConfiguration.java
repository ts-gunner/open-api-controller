package com.forty.wall.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "openapi.gateway")
public class NacosConfiguration {

    private List<String> whiteList;

}

package com.forty.sdk;

import com.forty.sdk.client.FortyClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("forty.client")
@Data
@ComponentScan
public class FortyClientConfig {

    private String secretId;

    private String secretKey;

    @Bean
    public FortyClient fortyClient() {
        return new FortyClient(secretId, secretKey);
    }
}

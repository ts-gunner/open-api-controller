package com.forty;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@EnableDubbo
@SpringBootApplication
@MapperScan("com.forty.mapper")
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class OpenApiBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenApiBackendApplication.class, args);
    }

}

package com.forty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class OpenApiInterfaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenApiInterfaceApplication.class, args);
    }

}

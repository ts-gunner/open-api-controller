package com.forty;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.forty.mapper")
public class OpenApiInterfaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenApiInterfaceApplication.class, args);
    }

}

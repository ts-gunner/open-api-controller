package com.forty.wall.constant;


import lombok.Getter;

@Getter
public class CommonConstant {

    public static final String[] DEFAULT_EXCLUDE_PATTERNS = new String[]{
            // backend open-api document
            "/controller/swagger-resources/**", "/controller/webjars/**",
            "/controller/v3/**", "/controller/swagger-ui.html/**",
            "/controller/api", "/controller/api-docs", "/controller/api-docs/**",
            "/controller/doc.html/**", "/controller/favicon.ico"
    };
}

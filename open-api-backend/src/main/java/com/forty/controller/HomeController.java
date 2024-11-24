package com.forty.controller;

import com.forty.common.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public BaseResponse<String> home() {
        return new BaseResponse<>("Welcome to open ai platform!!");
    }
}

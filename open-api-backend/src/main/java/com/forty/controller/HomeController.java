package com.forty.controller;

import com.forty.common.BaseResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name= "主页简易API")
public class HomeController {

    @GetMapping("/")
    public BaseResponse<String> home() {
        return new BaseResponse<>("Welcome to open ai platform!!");
    }
}

package com.forty.controller;

import com.forty.common.BaseResponse;
import com.forty.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/name")
public class NameController {

    @GetMapping("/getName")
    public BaseResponse<String> getName(@RequestParam String name, HttpServletRequest request) {
        return new BaseResponse<>("GET 你的名字是: " + name);
    }

    @GetMapping("/")
    public BaseResponse<String> getHomeName(@RequestParam String name) {
        return new BaseResponse<>("GET 主页名字是: " + name);
    }

    @PostMapping("/postName")
    public BaseResponse<String> postName(@RequestBody User user) {
        return new BaseResponse<>("POST 你的名字是: " + user.getUserName());
    }
}

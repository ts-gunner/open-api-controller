package com.forty.controller;

import com.forty.sdk.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/name")
public class NameController {

    @GetMapping("/getName")
    public String getName(@RequestParam String name, HttpServletRequest request) {
        System.out.println(request.getHeader("forty"));
        return "GET 你的名字是: " + name;
    }

    @GetMapping("/")
    public String getHomeName(@RequestParam String name) {
        return "主页名字是: " + name;
    }

    @PostMapping("/postName")
    public String postName(@RequestBody User user) {
        return "POST 你的名字是: " + user.getUserName();
    }
}

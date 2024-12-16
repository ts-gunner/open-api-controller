package com.forty.controller;

import com.forty.common.BaseResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chatbot")
public class ChatbotController {

    @RequestMapping("/ask")
    public BaseResponse<String> ask(@RequestParam String question) {
        return new BaseResponse<>("your question is " + question + "; \n I am a chat robot. I can help you answer some question.");
    }
}

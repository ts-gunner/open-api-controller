package com.forty.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.forty.common.BaseResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/daily_hot")
public class DailyHotController {


    @RequestMapping("/bilibili")
    public BaseResponse<Object> getBilibiliDailyHot() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Origin", "https://hot.imsyy.top");
        headers.put("Referer", "https://hot.imsyy.top/");
        headers.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36 Edg/131.0.0.0");
        HttpResponse response = HttpRequest.get("https://api-hot.imsyy.top/bilibili?cache=true")
                .addHeaders(headers)
                .execute();
        JSONObject entries = JSONUtil.parseObj(response.body());
        int code = (int) entries.get("code");
        Object data = null;
        if (code == 200)  data = entries.get("data");

        return new BaseResponse<>(data);

    }

    @RequestMapping("/douyin")
    public BaseResponse<Object> getDouyinDailyHot() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Origin", "https://hot.imsyy.top");
        headers.put("Referer", "https://hot.imsyy.top/");
        headers.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36 Edg/131.0.0.0");
        HttpResponse response = HttpRequest.get("https://api-hot.imsyy.top/douyin?cache=true")
                .addHeaders(headers)
                .execute();
        JSONObject entries = JSONUtil.parseObj(response.body());
        int code = (int) entries.get("code");
        Object data = null;
        if (code == 200)  data = entries.get("data");

        return new BaseResponse<>(data);

    }
}

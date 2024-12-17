package com.forty;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.forty.common.BaseResponse;
import com.forty.sdk.adaptor.BaseAdaptor;
import com.forty.sdk.adaptor.DailyHotAdaptor;
import com.forty.sdk.api.Chatbot;
import com.forty.sdk.client.FortyClient;

import java.util.HashMap;
import java.util.Map;

public class ClientMain {
    public static void main(String[] args) {
        // online
//        String secretId = "0b20b7aabfb1a8b6512360ed6903f219f3f09d6d5ebe509e98ceca16814a2ca6"; // test2
//        String secretKey = ">O%@9XCm35^L)n[V)#9-ou0yrN&dO,*|4xszT<_^";

        // location
        String secretId = "e8de092f5f9e91f6d451870cfcf6133b3a16ec4c44751e432efd5463e380b946";  // admin
        String secretKey = "GA)p}Y;ZG,vwol;r*6,^i,U@^U*3e|WppjxIUOP8";

//        String openApiUrl = "http://192.168.5.100:44388";
        String openApiUrl = "http://localhost:8445";
        FortyClient client = new FortyClient(secretId, secretKey, openApiUrl);
        BaseResponse<Object> bilibiliHot = client.adaptor(DailyHotAdaptor.class).getBilibiliHot();
//        BaseResponse<Object> tiktok = client.adaptor(DailyHotAdaptor.class).getTiktokHot();
        System.out.println(bilibiliHot);
//        System.out.println(tiktok);
//        client.adaptor(Chatbot.class).ask("who are you?");
//        ClientMain.testBilibili();
    }

    public static void testBilibili(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Origin", "https://hot.imsyy.top");
        headers.put("Referer", "https://hot.imsyy.top/");
        headers.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36 Edg/131.0.0.0");
        HttpResponse response = HttpRequest.get("https://api-hot.imsyy.top/bilibili?cache=true")
                .addHeaders(headers)
                .execute();
        System.out.println(response.body());
    }
}

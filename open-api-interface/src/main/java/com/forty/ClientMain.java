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
//        String secretId = "e8de092f5f9e91f6d451870cfcf6133b3a16ec4c44751e432efd5463e380b946"; // admin
        String secretId = "2bcbb957b4503e688390e5068f44be32f9c64eb86bd041bf031c5707d1cd80c4"; // test4
        String secretKey = "^f}19]oGx)IGdE]ny]JkhH1$Kv[-)QO0)S8(EIkN";
        FortyClient client = new FortyClient(secretId, secretKey);
        BaseResponse<Object> bilibiliHot = client.adaptor(DailyHotAdaptor.class).getBilibiliHot();
        BaseResponse<Object> tiktok = client.adaptor(DailyHotAdaptor.class).getTiktokHot();
        System.out.println(bilibiliHot);
        System.out.println(tiktok);
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

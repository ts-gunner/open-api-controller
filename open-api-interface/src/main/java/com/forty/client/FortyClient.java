package com.forty.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.forty.model.User;
import com.forty.utils.EncryptUtils;

import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class FortyClient {
    private String secretID;

    private String secretKey;

    public FortyClient(String secretID, String secretKey) {
        this.secretID = secretID;
        this.secretKey = secretKey;
    }


    public Map<String, String> getHeaderMap(String body) {
        Map<String, String> map = new HashMap<>();
        map.put("secretID", this.secretID);
        map.put("sign", generateSign(body));
        map.put("body", body);
        map.put("nonce", RandomUtil.randomNumbers(3));
        map.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        return map;
    }


    public String generateSign(String body) {
        return EncryptUtils.generateEncryptString(body + "." + this.secretKey);
    }


    public String getName(String name) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        String url = HttpUtil.urlWithForm("http://localhost:8444/api/name/getName", map, Charset.defaultCharset(), false);
        HttpResponse response = HttpRequest.get(url)
                .addHeaders(getHeaderMap(name))
                .execute();

        System.out.println(response.body());
        return response.body();
    }

    public String postName(User user) {
        String jsonStr = JSONUtil.toJsonStr(user);
        HttpResponse response = HttpRequest.post("http://localhost:8444/api/name/postName")
                .addHeaders(getHeaderMap(jsonStr))
                .body(jsonStr)
                .execute();
        int status = response.getStatus();
        System.out.println(MessageFormat.format("status: {0}", status));
        String body = response.body();
        System.out.println(body);
        return body;
    }

}

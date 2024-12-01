package com.forty.sdk.client;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.forty.sdk.model.User;
import com.forty.utils.EncryptUtils;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class FortyClient {
    private final String secretId;

    private final String secretKey;

    public FortyClient(String secretId, String secretKey) {
        this.secretId = secretId;
        this.secretKey = secretKey;
    }


    public Map<String, String> getHeaderMap(String body) {
        Map<String, String> map = new HashMap<>();
        map.put("secretID", this.secretId);
        map.put("sign", generateSign(body));
        map.put("body", Convert.strToUnicode(body));
        map.put("nonce", RandomUtil.randomNumbers(3));
        map.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        map.put("Content-Type", "application/json; charset=utf-8");
        return map;
    }


    public String generateSign(String body) {
        return EncryptUtils.generateEncryptString(body + "." + this.secretKey);
    }


    public String getName(String name) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        String url = HttpUtil.urlWithForm("http://localhost:8444/api/name/getName", map, CharsetUtil.CHARSET_UTF_8, false);
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

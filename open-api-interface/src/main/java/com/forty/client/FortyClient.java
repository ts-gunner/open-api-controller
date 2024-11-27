package com.forty.client;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.forty.model.User;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class FortyClient {
    private String secretID;

    private String secretKey;

    public FortyClient(String secretID, String secretKey) {
        this.secretID = this.secretID;
        this.secretKey = this.secretKey;
    }

    public String getName(String name) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        String result = HttpUtil.get("http://localhost:8444/api/name/getName", map);
        System.out.println(result);
        return result;
    }

    public Map<String, String> getHeaderMap() {
        Map<String, String> map = new HashMap<>();
        map.put("secretID", this.secretID);
        map.put("secretKey", this.secretKey);
        return map;
    }
    public String postName(User user) {
        String jsonStr = JSONUtil.toJsonStr(user);
        HttpResponse response = HttpRequest.post("http://localhost:8444/api/name/postName")
                .addHeaders(getHeaderMap())
                .body(jsonStr)
                .execute();
        int status = response.getStatus();
        System.out.println(MessageFormat.format("status: {0}", status));
        String body = response.body();
        System.out.println(body);
        return body;
    }

}

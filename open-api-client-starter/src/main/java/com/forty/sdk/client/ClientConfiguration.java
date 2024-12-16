package com.forty.sdk.client;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.RandomUtil;
import com.forty.utils.EncryptUtils;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ClientConfiguration {

    private final String salt = "forty";

    private String prefixUrl = "http://localhost:8445";

    private final String secretId;

    private final String secretKey;

    ClientConfiguration(String secretId, String secretKey) {
        this.secretId = secretId;
        this.secretKey = secretKey;
    }

    ClientConfiguration(String secretId, String secretKey, String public_open_api_url) {
        this(secretId, secretKey);
        this.prefixUrl = public_open_api_url;
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



}

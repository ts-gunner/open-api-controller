package com.forty.sdk.adaptor;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.forty.common.BaseResponse;
import com.forty.sdk.client.ClientConfiguration;
import lombok.Data;

import java.util.Map;


@Data
public class BaseAdaptor {
    private ClientConfiguration configuration;

    BaseAdaptor(ClientConfiguration configuration) {
        this.configuration = configuration;
    }

    public boolean testApiStatus(String url, String method){
        HttpResponse response;
        Map<String, String> headerMap = configuration.getHeaderMap(configuration.getSalt());
        headerMap.put("test-api-skip-authorization", "true");

        if (method.equalsIgnoreCase("GET")){
            response = HttpRequest.get(url)
                    .addHeaders(headerMap)
                    .execute();

        }else {
            // todo: post需要传body
            response = HttpRequest.post(url)
                    .addHeaders(headerMap)
                    .execute();

        }
        JSONObject entries = JSONUtil.parseObj(response.body());
        return response.getStatus() == HttpStatus.HTTP_OK && (int) entries.get("code") == HttpStatus.HTTP_OK;
    }

    public BaseResponse<Object> callApiOnline(String url, String method){
        HttpResponse response;
        Map<String, String> headerMap = configuration.getHeaderMap(configuration.getSalt());
        headerMap.put("test-api-skip-authorization", "true");
        if (method.equalsIgnoreCase("GET")){
            response = HttpRequest.get(url)
                    .addHeaders(headerMap)
                    .execute();

        }else {
            response = HttpRequest.post(url)
                    .addHeaders(headerMap)
                    .execute();

        }
        JSONObject entries = JSONUtil.parseObj(response.body());
        return BeanUtil.copyProperties(entries, BaseResponse.class);
    }
}

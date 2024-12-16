package com.forty.sdk.adaptor;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.forty.common.BaseResponse;
import com.forty.sdk.api.DailyHot;
import com.forty.sdk.client.ClientConfiguration;


public class DailyHotAdaptor extends BaseAdaptor implements DailyHot {
    DailyHotAdaptor(ClientConfiguration configuration) {
        super(configuration);
    }

    @Override
    public BaseResponse<Object> getTiktokHot() {
        HttpResponse response = HttpRequest.get(this.getConfiguration().getPrefixUrl() + "/public_api/daily_hot/douyin")
                .addHeaders(this.getConfiguration().getHeaderMap(this.getConfiguration().getSalt()))
                .execute();
        String body = response.body();
        JSONObject jsonObject = JSONUtil.parseObj(body);
        return BeanUtil.copyProperties(jsonObject, BaseResponse.class);
    }

    @Override
    public BaseResponse<Object> getBilibiliHot() {
        HttpResponse response = HttpRequest.get(this.getConfiguration().getPrefixUrl() + "/public_api/daily_hot/bilibili")
                .addHeaders(this.getConfiguration().getHeaderMap(this.getConfiguration().getSalt()))
                .execute();
        String body = response.body();
        JSONObject jsonObject = JSONUtil.parseObj(body);
        return BeanUtil.copyProperties(jsonObject, BaseResponse.class);
    }
}

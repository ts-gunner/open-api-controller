package com.forty.sdk.api;


import com.forty.common.BaseResponse;

public interface DailyHot {
    BaseResponse<Object> getTiktokHot();

    BaseResponse<Object> getBilibiliHot();
}

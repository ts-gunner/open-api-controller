package com.forty.remote;

import com.forty.common.BaseResponse;

public interface InterfaceInnerService {

    BaseResponse<Object> verifyInterfaceAvailable(Long userId, Integer interfaceId);

    // 接口调用次数统计
    boolean invokeInterfaceCount(Long userId, Integer interfaceId);
}

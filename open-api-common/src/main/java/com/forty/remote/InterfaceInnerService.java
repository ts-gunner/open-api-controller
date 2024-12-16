package com.forty.remote;

import com.forty.common.BaseResponse;

public interface InterfaceInnerService {

    boolean enableVerifyInterface();

    BaseResponse<Integer> verifyInterfaceAvailable(Long userId, String apiPath);

    // 接口调用次数统计
    boolean invokeInterfaceCount(Long userId, Integer interfaceId);
}

package com.forty.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.forty.model.dto.userinterfaceinfo.UserInterfaceQueryRequest;
import com.forty.model.entity.TokenData;
import com.forty.model.entity.UserInterfaceInfo;
import com.forty.model.vo.UserInterfaceVO;

public interface UserInterfaceService extends IService<UserInterfaceInfo> {
    Page<UserInterfaceVO> getUserInterfaceInfoList(UserInterfaceQueryRequest request);
    Page<UserInterfaceVO> getUserInterfaceInfoList(UserInterfaceQueryRequest request, TokenData tokenData);
}

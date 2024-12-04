package com.forty.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.forty.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import com.forty.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.forty.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import com.forty.model.entity.InterfaceInfo;
import com.forty.model.vo.InterfaceInfoVO;

public interface InterfaceService extends IService<InterfaceInfo> {

    void addInterface(InterfaceInfoAddRequest request, String userAccount);

    Page<InterfaceInfoVO> queryInterface(InterfaceInfoQueryRequest request);

    QueryWrapper<InterfaceInfo> getInterfaceQueryWrapper(InterfaceInfoQueryRequest request);

    int deleteInterface(int interfaceId);

    int updateInterface(InterfaceInfoUpdateRequest request);

    void publishInterface(int interfaceId);

    void demiseInterface(int interfaceId);
}

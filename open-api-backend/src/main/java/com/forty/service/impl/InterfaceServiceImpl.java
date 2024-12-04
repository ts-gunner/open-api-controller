package com.forty.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.forty.common.CodeStatus;
import com.forty.exception.BusinessException;
import com.forty.mapper.InterfaceInfoMapper;
import com.forty.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import com.forty.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.forty.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import com.forty.model.entity.InterfaceInfo;
import com.forty.model.vo.InterfaceInfoVO;
import com.forty.sdk.client.FortyClient;
import com.forty.service.InterfaceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterfaceServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements InterfaceService {

    @Autowired
    FortyClient fortyClient;

    @Override
    public void addInterface(InterfaceInfoAddRequest request, String userAccount) {

        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", request.getInterfaceName());
        InterfaceInfo interfaceInfoObject = this.baseMapper.selectOne(queryWrapper);
        if (interfaceInfoObject != null) throw new BusinessException(CodeStatus.FAIL, "该接口已添加，请勿重复操作");
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setName(request.getInterfaceName());
        interfaceInfo.setDescription(request.getDescription());
        interfaceInfo.setUrl(request.getUrl());
        interfaceInfo.setMethod(request.getMethod().toUpperCase());
        interfaceInfo.setRequestHeader(request.getRequestHeader());
        interfaceInfo.setResponseHeader(request.getResponseHeader());
        interfaceInfo.setRequestBody(request.getRequestBody());
        interfaceInfo.setResponseBody(request.getResponseBody());
        interfaceInfo.setUserAccount(userAccount);
        boolean result = this.save(interfaceInfo);
        if (!result) throw new BusinessException(CodeStatus.DB_ERROR, "数据库写入异常");
    }

    @Override
    public QueryWrapper<InterfaceInfo> getInterfaceQueryWrapper(InterfaceInfoQueryRequest request) {
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        Integer interfaceId = request.getInterfaceId();
        String interfaceName = request.getInterfaceName();
        String methodName = request.getMethod();
        String userAccount = request.getUserAccount();
        Boolean status = request.getStatus();
        queryWrapper.eq(interfaceId != null, "id", interfaceId);
        queryWrapper.like(!StringUtils.isBlank(interfaceName), "name", interfaceName);
        if (!StringUtils.isBlank(methodName)) queryWrapper.eq("method", methodName.toUpperCase());
        queryWrapper.like(!StringUtils.isBlank(userAccount), "user_account", userAccount);
        queryWrapper.eq(status != null ,"status", status);
        return queryWrapper;
    }

    @Override
    public Page<InterfaceInfoVO> queryInterface(InterfaceInfoQueryRequest request) {
        IPage<InterfaceInfo> interfaceInfoIPage = new Page<>(request.getCurrentPage(), request.getPageSize());
        QueryWrapper<InterfaceInfo> queryWrapper = getInterfaceQueryWrapper(request);
        IPage<InterfaceInfo> page = this.baseMapper.selectPage(interfaceInfoIPage, queryWrapper);
        Page<InterfaceInfoVO> interfaceInfoVOPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<InterfaceInfoVO> list = page.getRecords().stream().map(record -> {
            InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
            BeanUtils.copyProperties(record, interfaceInfoVO);
            return interfaceInfoVO;
        }).toList();
        interfaceInfoVOPage.setRecords(list);

        return interfaceInfoVOPage;
    }

    @Override
    public int deleteInterface(int interfaceId) {
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", interfaceId);
        return this.baseMapper.delete(queryWrapper);
    }

    @Override
    public int updateInterface(InterfaceInfoUpdateRequest request) {
        InterfaceInfo interfaceInfo = this.baseMapper.selectById(request.getInterfaceId());
        if (interfaceInfo == null) throw new BusinessException(CodeStatus.DATA_NOT_EXIST, "接口找不到");

        String interfaceName = request.getInterfaceName();
        String description = request.getInterfaceDescription();
        Boolean status = request.getStatus();
        String method = request.getMethod();
        String requestHeader = request.getRequestHeader();
        String responseHeader = request.getResponseHeader();
        String requestBody = request.getRequestBody();
        String responseBody = request.getResponseBody();
        String url = request.getUrl();
        UpdateWrapper<InterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", interfaceInfo.getId());
        updateWrapper.set(!StringUtils.isBlank(interfaceName), "name", interfaceName);
        updateWrapper.set(!StringUtils.isBlank(method), "method", method.toUpperCase());
        updateWrapper.set(!StringUtils.isBlank(description), "description", description);
        updateWrapper.set(!StringUtils.isBlank(requestHeader), "request_header", requestHeader);
        updateWrapper.set(!StringUtils.isBlank(responseHeader), "response_header", responseHeader);
        updateWrapper.set(!StringUtils.isBlank(requestBody), "request_body", requestBody);
        updateWrapper.set(!StringUtils.isBlank(responseBody), "response_body", responseBody);
        updateWrapper.set(!StringUtils.isBlank(url), "url", request.getUrl());
        updateWrapper.set(status != null, "status", status);

        return this.baseMapper.update(updateWrapper);
    }

    @Override
    public void publishInterface(int interfaceId) {
        InterfaceInfo interfaceInfo = this.baseMapper.selectById(interfaceId);
        if (interfaceInfo == null) throw new BusinessException(CodeStatus.DATA_NOT_EXIST, "接口不存在");

        // 校验接口是否可用
        String test = fortyClient.getName("test");
        if (StringUtils.isBlank(test)) throw new BusinessException(CodeStatus.INTERFACE_CALL_FAILED, "接口响应失败");

        interfaceInfo.setStatus(true);
        this.updateById(interfaceInfo);
    }

    @Override
    public void demiseInterface(int interfaceId) {
        InterfaceInfo interfaceInfo = this.baseMapper.selectById(interfaceId);
        if (interfaceInfo == null) throw new BusinessException(CodeStatus.DATA_NOT_EXIST, "接口不存在");

        interfaceInfo.setStatus(false);
        this.updateById(interfaceInfo);
    }
}

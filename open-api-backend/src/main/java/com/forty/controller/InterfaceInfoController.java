package com.forty.controller;


import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.forty.annotation.RoleCheck;
import com.forty.common.BaseResponse;
import com.forty.common.CodeStatus;
import com.forty.config.NacosConfig;
import com.forty.exception.BusinessException;
import com.forty.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import com.forty.model.dto.interfaceinfo.InterfaceInfoInvokeRequest;
import com.forty.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.forty.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import com.forty.model.entity.InterfaceInfo;
import com.forty.model.entity.SecretInfo;
import com.forty.model.entity.TokenData;
import com.forty.model.vo.InterfaceInfoVO;
import com.forty.sdk.adaptor.BaseAdaptor;
import com.forty.sdk.adaptor.DailyHotAdaptor;
import com.forty.sdk.client.FortyClient;
import com.forty.service.InterfaceService;
import com.forty.service.SecretInfoService;
import com.forty.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/openapi")
public class InterfaceInfoController {

    @Autowired
    InterfaceService interfaceService;

    @Autowired
    SecretInfoService secretInfoService;

    @Autowired
    UserService userService;

    @Autowired
    NacosConfig nacosConfig;
    /**
     * 接口的增删改查
     */
    @PostMapping("/add")
    @RoleCheck(hasRoles = {"superadmin", "admin"})
    public BaseResponse<Object> addInterfaceInfo(
            @RequestAttribute("tokenData") TokenData tokenData,
            @RequestBody InterfaceInfoAddRequest request) {
        interfaceService.addInterface(request,tokenData.getUserAccount());
        return new BaseResponse<>();
    }

    @PostMapping("/query")
    public BaseResponse<Page<InterfaceInfoVO>> queryInterfaceInfo(@RequestBody InterfaceInfoQueryRequest request) {
        Page<InterfaceInfoVO> interfaceInfoVOPage = interfaceService.queryInterface(request);
        return new BaseResponse<>(interfaceInfoVOPage);
    }


    @PostMapping("/queryAll")
    public BaseResponse<List<InterfaceInfoVO>> queryAllInterfaceInfo(@RequestBody InterfaceInfoQueryRequest request) {
        QueryWrapper<InterfaceInfo> interfaceQueryWrapper = interfaceService.getInterfaceQueryWrapper(request);
        List<InterfaceInfo> list = interfaceService.list(interfaceQueryWrapper);
        List<InterfaceInfoVO> interfaceInfoVOList = list.stream().map(item -> {
            InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
            BeanUtils.copyProperties(item, interfaceInfoVO);
            return interfaceInfoVO;
        }).toList();
        return new BaseResponse<>(interfaceInfoVOList);
    }

    @GetMapping("/delete")
    @RoleCheck(requiredRoles = "superadmin")
    public BaseResponse<Integer> deleteInterface(@RequestParam("interfaceId") Integer interfaceId){
        return new BaseResponse<>(interfaceService.deleteInterface(interfaceId));
    }

    @PostMapping("/update")
    @RoleCheck(hasRoles = {"superadmin", "admin"})
    public BaseResponse<Integer> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest request){
        return new BaseResponse<>(interfaceService.updateInterface(request));
    }

    /**
     * 发布接口
     * @param interfaceId
     * @return
     */
    @GetMapping("/online")
    @RoleCheck(hasRoles = {"admin", "superadmin"})
    public BaseResponse<Integer> publishInterface(@RequestParam("interfaceId") Integer interfaceId){
        InterfaceInfo interfaceInfo = interfaceService.getBaseMapper().selectById(interfaceId);
        if (interfaceInfo == null) throw new BusinessException(CodeStatus.DATA_NOT_EXIST, "接口不存在");

        FortyClient client = nacosConfig.getOpenApiUrl() != null ?
                new FortyClient(nacosConfig.getSecretId(), nacosConfig.getSecretKey(), nacosConfig.getOpenApiUrl())
                : new FortyClient(nacosConfig.getSecretId(), nacosConfig.getSecretKey());
        // 校验接口是否可用
        boolean testResult = client.adaptor(BaseAdaptor.class).testApiStatus(interfaceInfo.getUrl(), interfaceInfo.getMethod());
        if (!testResult) throw new BusinessException(CodeStatus.INTERFACE_CALL_FAILED, "接口响应失败");

        interfaceInfo.setStatus(true);
        interfaceService.getBaseMapper().updateById(interfaceInfo);
        return new BaseResponse<>();
    }

    /**
     * 下线接口
     * @param interfaceId
     * @return
     */
    @GetMapping("/offline")
    @RoleCheck(hasRoles = {"superadmin", "admin"})
    public BaseResponse<Integer> demiseInterface(@RequestParam("interfaceId") Integer interfaceId){
        InterfaceInfo interfaceInfo = interfaceService.getBaseMapper().selectById(interfaceId);
        if (interfaceInfo == null) throw new BusinessException(CodeStatus.DATA_NOT_EXIST, "接口不存在");

        interfaceInfo.setStatus(false);
        int result = interfaceService.getBaseMapper().updateById(interfaceInfo);
        return new BaseResponse<>(result);
    }

    /**
     * 在线调试API
     */
    @PostMapping("/invoke")
    public BaseResponse<Object> invokeInterface(
            @RequestBody InterfaceInfoInvokeRequest request,
            @RequestAttribute("tokenData") TokenData tokenData){

        InterfaceInfo interfaceInfo = interfaceService.getById(request.getInterfaceId());
        if (interfaceInfo == null) throw new BusinessException(CodeStatus.INTERFACE_CALL_FAILED, "接口不存在");
        if (!interfaceInfo.getStatus()) throw new BusinessException(CodeStatus.INTERFACE_CALL_FAILED, "接口已关闭");
        String secretId = userService.getById(tokenData.getUserId()).getSecretId();
        SecretInfo secretObject = secretInfoService.getSecretObject(tokenData.getUserId());
        if (secretObject == null) throw new BusinessException(CodeStatus.INTERFACE_CALL_FAILED, "没有密钥key，请先创建一个再试！");
        String secretKey = secretObject.getSecretKey();
        FortyClient client = new FortyClient(secretId, secretKey);
        // todo: 调用API
        BaseResponse<Object> response = client.adaptor(BaseAdaptor.class).callApiOnline(interfaceInfo.getUrl(), interfaceInfo.getMethod());
        System.out.println(response);
        return response;
    }

}

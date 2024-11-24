package com.forty.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.forty.common.BaseResponse;
import com.forty.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import com.forty.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.forty.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import com.forty.model.entity.TokenData;
import com.forty.model.vo.InterfaceInfoVO;
import com.forty.service.InterfaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/openapi")
public class InterfaceInfoController {

    @Autowired
    InterfaceService interfaceService;
    /**
     * 接口的增删改查
     */
    @PostMapping("/add")
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

    @GetMapping("/delete")
    public BaseResponse<Integer> deleteInterface(@RequestParam("interfaceId") Integer interfaceId){
        return new BaseResponse<>(interfaceService.deleteInterface(interfaceId));
    }

    @PostMapping("/update")
    public BaseResponse<Integer> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest request){
        return new BaseResponse<>(interfaceService.updateInterface(request));
    }
}

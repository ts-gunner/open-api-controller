package com.forty.controller;


import com.forty.common.BaseResponse;
import com.forty.mapper.InterfaceInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api_analysis")
public class AnalysisController {

    @Autowired
    InterfaceInfoMapper interfaceInfoMapper;

    /**
     * 获取接口的调用情况
     */
    @GetMapping("/get_interface_situation")
    public BaseResponse<Object> getInterfaceCallingSituation() {
        return new BaseResponse<>();
    }

}

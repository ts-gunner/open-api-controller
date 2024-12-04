package com.forty.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.forty.annotation.RoleCheck;
import com.forty.common.BaseResponse;
import com.forty.common.CodeStatus;
import com.forty.exception.BusinessException;
import com.forty.model.dto.userinterfaceinfo.UserInterfaceAddRequest;
import com.forty.model.dto.userinterfaceinfo.UserInterfaceQueryRequest;
import com.forty.model.dto.userinterfaceinfo.UserInterfaceUpdateRequest;
import com.forty.model.entity.TokenData;
import com.forty.model.entity.UserInterfaceInfo;
import com.forty.model.vo.UserInterfaceVO;
import com.forty.service.UserInterfaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user_interface")
public class UserInterfaceInfoController {

    @Autowired
    UserInterfaceService userInterfaceService;
    /**
     * 增或者更新
     */
    @PostMapping("/add")
    @RoleCheck(hasRoles = {"superadmin", "admin"})
    public BaseResponse<Object> addOrUpdateUserInterfaceInfo(@RequestBody UserInterfaceAddRequest request) {
        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", request.getUserId());
        updateWrapper.eq("interface_id", request.getInterfaceId());
        updateWrapper.set("remain_count", request.getRemainCount());

        int updateResult = userInterfaceService.getBaseMapper().update(updateWrapper);
        if (updateResult == 0) {
            UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
            userInterfaceInfo.setInterfaceId(request.getInterfaceId());
            userInterfaceInfo.setUserId(request.getUserId());
            userInterfaceInfo.setRemainCount(request.getRemainCount());
            boolean save = userInterfaceService.save(userInterfaceInfo);
            if (!save) throw new BusinessException(CodeStatus.DB_ERROR, "添加异常");
        }

        return new BaseResponse<>();
    }

    /**
     * 删
     */
    @GetMapping("/delete")
    @RoleCheck(hasRoles = {"superadmin", "admin"})
    public BaseResponse<Boolean> deleteUserInterfaceInfo(@RequestParam Integer userInterfaceId) {
        boolean result = userInterfaceService.removeById(userInterfaceId);
        return new BaseResponse<>(result);
    }

    /**
     * 更新
     */
    @PostMapping("/update")
    @RoleCheck(hasRoles = {"superadmin", "admin"})
    public BaseResponse<Object> updateUserInterfaceInfo(@RequestBody UserInterfaceUpdateRequest request) {

        return new BaseResponse<>(CodeStatus.FAIL.getCode(), "暂未实现");
    }

    /**
     * 查
     */
    @PostMapping("/search")
    @RoleCheck(hasRoles = {"superadmin", "admin"})
    public BaseResponse<Page<UserInterfaceVO>> searchUserInterfaceInfo(@RequestBody UserInterfaceQueryRequest request) {
        Page<UserInterfaceVO> userInterfaceInfoList = userInterfaceService.getUserInterfaceInfoList(request);
        return new BaseResponse<>(userInterfaceInfoList);
    }
    /**
     * 用户查询API目录
     */
    @PostMapping("/searchByUser")
    public BaseResponse<Page<UserInterfaceVO>> searchUserInterfaceInfoByUser(
            @RequestBody UserInterfaceQueryRequest request,
            @RequestAttribute("tokenData") TokenData tokenData
    ) {
        Page<UserInterfaceVO> userInterfaceInfoList = userInterfaceService.getUserInterfaceInfoList(request, tokenData);
        return new BaseResponse<>(userInterfaceInfoList);
    }
}

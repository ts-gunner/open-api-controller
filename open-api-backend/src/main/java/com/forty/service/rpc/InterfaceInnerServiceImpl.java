package com.forty.service.rpc;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.forty.common.BaseResponse;
import com.forty.common.CodeStatus;
import com.forty.mapper.RoleAssignmentMapper;
import com.forty.mapper.UserInterfaceInfoMapper;
import com.forty.model.entity.InterfaceInfo;
import com.forty.model.entity.UserInterfaceInfo;
import com.forty.model.vo.RoleAssignmentVO;
import com.forty.remote.InterfaceInnerService;
import com.forty.sdk.client.FortyClient;
import com.forty.service.InterfaceService;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.List;

@DubboService
public class InterfaceInnerServiceImpl implements InterfaceInnerService {

    @Resource
    InterfaceService interfaceService;

    @Resource
    UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Resource
    RoleAssignmentMapper roleAssignmentMapper;

    @Resource
    FortyClient fortyClient;

    @Override
    public boolean enableVerifyInterface() {
        return false;
    }

    @Override
    public BaseResponse<Integer> verifyInterfaceAvailable(Long userId, String apiPath) {

        QueryWrapper<InterfaceInfo> interfaceInfoQueryWrapper = new QueryWrapper<>();
        interfaceInfoQueryWrapper.eq("url", apiPath);
        InterfaceInfo interfaceInfo = interfaceService.getBaseMapper().selectOne(interfaceInfoQueryWrapper);
        if (interfaceInfo == null) return new BaseResponse<>(CodeStatus.INTERFACE_CALL_FAILED.getCode(), "接口不存在");
        if (!interfaceInfo.getStatus()) return new BaseResponse<>(CodeStatus.INTERFACE_CALL_FAILED.getCode(), "接口已关闭");

        // 查询用户是否是超级管理员或者管理员
        // 超级管理员可以直接跳过校验调用次数
        List<RoleAssignmentVO> roleAssignmentList = roleAssignmentMapper.getRoleAssignmentList(null, null, userId);
        List<String> roles = roleAssignmentList.stream().map(RoleAssignmentVO::getRoleName).toList();
        if (roles.contains("admin") || roles.contains("superadmin")) return new BaseResponse<>(interfaceInfo.getId());

        // 查询用户是否有调用次数
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("interface_id", interfaceInfo.getId());
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("status", 1);
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoMapper.selectOne(queryWrapper);
        if (userInterfaceInfo == null) return new BaseResponse<>(CodeStatus.INTERFACE_CALL_FAILED.getCode(), "用户没有申请调用该接口");
        if (userInterfaceInfo.getRemainCount() <= 0) return new BaseResponse<>(CodeStatus.INTERFACE_CALL_FAILED.getCode(), "用户已用完调用该接口的次数");

        return new BaseResponse<>(interfaceInfo.getId());
    }

    @Override
    public boolean invokeInterfaceCount(Long userId, Integer interfaceId) {
        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("interface_id", interfaceId);
        updateWrapper.eq("user_id", userId);
        updateWrapper.eq("status", 1);
        synchronized (this){
            // 用户接口记录的总调用次数 + 1
            updateWrapper.setSql("total_count = total_count + 1");
            // 用户接口记录的剩余调用次数 - 1
            updateWrapper.setSql("remain_count = remain_count - 1");
            userInterfaceInfoMapper.update(updateWrapper);
            // 接口总调用次数 + 1
            UpdateWrapper<InterfaceInfo> interfaceInfoUpdateWrapper = new UpdateWrapper<>();
            interfaceInfoUpdateWrapper.eq("id", interfaceId);
            interfaceInfoUpdateWrapper.eq("status", 1);
            interfaceInfoUpdateWrapper.setSql("total_calls = total_calls + 1");
            interfaceService.update(interfaceInfoUpdateWrapper);
            return true;
        }
    }
}

package com.forty.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.forty.common.CodeStatus;
import com.forty.exception.BusinessException;
import com.forty.mapper.RoleAssignmentMapper;
import com.forty.mapper.UserInfoMapper;
import com.forty.mapper.UserRoleMapper;
import com.forty.model.dto.roleassignment.RoleAssignmentQueryRequest;
import com.forty.model.entity.RoleAssignment;
import com.forty.model.entity.UserInfo;
import com.forty.model.entity.UserRole;
import com.forty.model.vo.RoleAssignmentVO;
import com.forty.service.RoleAssignmentService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleAssignmentServiceImpl extends ServiceImpl<RoleAssignmentMapper, RoleAssignment>
        implements RoleAssignmentService {

    @Resource
    UserInfoMapper userInfoMapper;

    @Resource
    UserRoleMapper userRoleMapper;

    @Override
    public Page<RoleAssignmentVO> getUserRoleMapVOList(RoleAssignmentQueryRequest request) {
        Page<RoleAssignmentVO> roleMapVOPage = new Page<>(request.getCurrentPage(), request.getPageSize());
        QueryWrapper<RoleAssignment> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(!StringUtils.isBlank(request.getRoleName()), "role_name", request.getRoleName());
        queryWrapper.like(!StringUtils.isBlank(request.getUserAccount()), "user_account", request.getUserAccount());
        List<RoleAssignmentVO> userRoleMapList = this.baseMapper.getRoleAssignmentList(roleMapVOPage, queryWrapper);
        roleMapVOPage.setRecords(userRoleMapList);
        roleMapVOPage.setTotal(userRoleMapList.size());
        return roleMapVOPage;
    }

    @Override
    public void addRoleToUser(long userId, int roleId) {
        // 查看是否有该记录
        QueryWrapper<RoleAssignment> assignmentQueryWrapper = new QueryWrapper<>();
        assignmentQueryWrapper.eq("role_id", roleId);
        assignmentQueryWrapper.eq("user_id", userId);
        RoleAssignment roleAssignmentObject = this.baseMapper.selectOne(assignmentQueryWrapper);
        if (roleAssignmentObject != null) throw new BusinessException(CodeStatus.FAIL, "已授权, 请勿重复授权");
        // 查找是否有该用户
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("id", userId);
        UserInfo userInfo = userInfoMapper.selectOne(userInfoQueryWrapper);
        if (userInfo == null) throw new BusinessException(CodeStatus.DATA_NOT_EXIST, "找不到该用户");
        // 查找是否有对应的角色
        QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.eq("role_id", roleId);
        UserRole userRole = userRoleMapper.selectOne(userRoleQueryWrapper);
        if (userRole == null) throw new BusinessException(CodeStatus.DATA_NOT_EXIST, "找不到该角色");

        RoleAssignment roleAssignment = new RoleAssignment();
        roleAssignment.setRoleId(roleId);
        roleAssignment.setUserId(userId);
        boolean result = this.save(roleAssignment);
        if (!result) throw new BusinessException(CodeStatus.SYSTEM_ERROR, "数据库添加失败");
    }
}

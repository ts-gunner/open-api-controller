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
        Page<RoleAssignmentVO> roleMapVOPage = new Page<>();
        List<RoleAssignmentVO> userRoleMapList = this.baseMapper.getRoleAssignmentList();
        roleMapVOPage.setRecords(userRoleMapList);
        return roleMapVOPage;
    }

    @Override
    public void addRoleToUser(long userId, int roleId) {
        // 查找是否有该用户
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        UserInfo userInfo = userInfoMapper.selectOne(queryWrapper);
        if (userInfo == null) throw new BusinessException(CodeStatus.DATA_NOT_EXIST, "找不到该用户");
        // 查找是否有对应的角色
        QueryWrapper<UserRole> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("role_id", roleId);
        UserRole userRole = userRoleMapper.selectOne(queryWrapper1);
        if (userRole == null) throw new BusinessException(CodeStatus.DATA_NOT_EXIST, "找不到该角色");

        RoleAssignment roleAssignment = new RoleAssignment();
        roleAssignment.setRoleId(roleId);
        roleAssignment.setUserId(userId);
        boolean result = this.save(roleAssignment);
        if (!result) throw new BusinessException(CodeStatus.SYSTEM_ERROR, "数据库添加失败");
    }
}

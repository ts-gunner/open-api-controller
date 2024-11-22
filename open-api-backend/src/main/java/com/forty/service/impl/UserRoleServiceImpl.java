package com.forty.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.forty.common.CodeStatus;
import com.forty.constant.CommonConstant;
import com.forty.exception.BusinessException;
import com.forty.mapper.RoleAssignmentMapper;
import com.forty.mapper.UserInfoMapper;
import com.forty.mapper.UserRoleMapper;
import com.forty.model.dto.role.UserRoleQueryRequest;
import com.forty.model.entity.UserRole;
import com.forty.model.vo.UserRoleVO;
import com.forty.service.UserRoleService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
        implements UserRoleService {

    @Resource
    private RoleAssignmentMapper roleAssignmentMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public int addRole(String roleName) {
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_name", roleName);
        UserRole role = this.baseMapper.selectOne(queryWrapper);
        if (role != null) throw new BusinessException(CodeStatus.FAIL, "角色已存在");
        UserRole userRole = new UserRole();
        userRole.setRoleName(roleName);
        boolean result = this.save(userRole);
        if (!result) throw new BusinessException(CodeStatus.SYSTEM_ERROR, "数据库添加失败");
        return userRole.getRoleId();
    }



    public QueryWrapper<UserRole> getUserRoleWrapper(UserRoleQueryRequest request) {
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(!StringUtils.isBlank(request.getRoleName()), "role_name", request.getRoleName());
        queryWrapper.orderBy(
                !StringUtils.isBlank(request.getSortField()),
                request.getSortOrder().equals(CommonConstant.SORT_ORDER_ASC),
                request.getSortField()
        );
        return queryWrapper;
    }


    @Override
    public int deleteUserRolesByRoleId(int roleId) {
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);
        UserRole userRole = this.baseMapper.selectOne(queryWrapper);
        if (userRole == null) throw new BusinessException(CodeStatus.PARAM_ERROR, "角色不存在");
        return this.baseMapper.deleteById(userRole);
    }

    @Override
    public Page<UserRoleVO> getUserRoleVOList(UserRoleQueryRequest request) {
        Page<UserRole> page = new Page<>(request.getCurrentPage(), request.getPageSize());
        QueryWrapper<UserRole> queryWrapper = getUserRoleWrapper(request);
        Page<UserRole> userRolePage = this.baseMapper.selectPage(page, queryWrapper);
        Page<UserRoleVO> pageVO = new Page<>(userRolePage.getCurrent(), userRolePage.getSize(),userRolePage.getTotal());
        List<UserRole> records = userRolePage.getRecords();
        List<UserRoleVO> userRoleVOs = records.stream().map((role) -> {
            UserRoleVO userRoleVO = new UserRoleVO();
            BeanUtils.copyProperties(role, userRoleVO);
            return userRoleVO;
        }).toList();
        pageVO.setRecords(userRoleVOs);
        return pageVO;
    }


}

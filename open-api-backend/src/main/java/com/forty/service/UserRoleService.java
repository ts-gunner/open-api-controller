package com.forty.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.forty.model.dto.role.UserRoleQueryRequest;
import com.forty.model.entity.UserRole;
import com.forty.model.vo.UserRoleVO;

public interface UserRoleService extends IService<UserRole> {

    int addRole(String roleName);

    int deleteUserRolesByRoleId(int roleId);

    Page<UserRoleVO> getUserRoleVOList(UserRoleQueryRequest request);


}

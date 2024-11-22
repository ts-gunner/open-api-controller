package com.forty.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.forty.model.dto.roleassignment.RoleAssignmentQueryRequest;
import com.forty.model.entity.RoleAssignment;
import com.forty.model.vo.RoleAssignmentVO;

public interface RoleAssignmentService extends IService<RoleAssignment> {
    Page<RoleAssignmentVO> getUserRoleMapVOList(RoleAssignmentQueryRequest request);

    void addRoleToUser(long userId, int roleId);

}

package com.forty.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.forty.common.BaseResponse;
import com.forty.model.dto.role.AddUserRoleRequest;
import com.forty.model.dto.roleassignment.RoleAssignmentQueryRequest;
import com.forty.model.vo.RoleAssignmentVO;
import com.forty.service.RoleAssignmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role/assignment")
@Tag(name = "角色授权")
public class RoleAssignmentController {

    @Autowired
    RoleAssignmentService roleAssignmentService;

    /**
     * 给用户添加角色
     */
    @PostMapping("/addRoleToUser")
    public BaseResponse<Object> addRoleToUser(@RequestBody AddUserRoleRequest request) {
        roleAssignmentService.addRoleToUser(request.getUserId(), request.getRoleId());
        return new BaseResponse<>();
    }

    /**
     * 获取用户授权角色的信息
     */
    @PostMapping("/get_assignment")
    public BaseResponse<Page<RoleAssignmentVO>> getAssignment(@RequestBody RoleAssignmentQueryRequest request) {
        Page<RoleAssignmentVO> page = roleAssignmentService.getUserRoleMapVOList(request);
        return new BaseResponse<>(page);
    }

}

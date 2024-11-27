package com.forty.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.forty.common.BaseResponse;
import com.forty.model.dto.role.UserRoleQueryRequest;
import com.forty.model.vo.UserRoleVO;
import com.forty.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
public class UserRoleController {

    @Autowired
    UserRoleService userRoleService;

    /**
     * 添加角色
     */
    @GetMapping("/add")
    public BaseResponse<Integer> addRole(@RequestParam("role") String roleName) {
        return new BaseResponse<>(userRoleService.addRole(roleName));
    }

    /**
     * 获取角色信息
     */
    @PostMapping("/getRoleByPage")
    public BaseResponse<Page<UserRoleVO>> getRoleByPage(@RequestBody UserRoleQueryRequest request) {
        Page<UserRoleVO> userRoleVOList = userRoleService.getUserRoleVOList(request);
        return new BaseResponse<>(userRoleVOList);
    }

    @PostMapping("/getRoleList")
    public BaseResponse<List<UserRoleVO>> getRoleList(@RequestBody UserRoleQueryRequest request) {
        List<UserRoleVO> roleList = userRoleService.getRoleList(request);
        return new BaseResponse<>(roleList);
    }

    /**
     * 删除角色信息
     */
    @GetMapping("/delete")
    public BaseResponse<Integer> deleteRole(@RequestParam("roleId") int roleId) {
        return new BaseResponse<>(userRoleService.deleteUserRolesByRoleId(roleId));
    }
}

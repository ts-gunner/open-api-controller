package com.forty.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.forty.common.BaseResponse;
import com.forty.model.dto.role.AddUserRoleRequest;
import com.forty.model.dto.role.UserRoleQueryRequest;
import com.forty.model.vo.UserRoleVO;
import com.forty.service.UserRoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role")
@Tag(name = "用户角色")
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
    @PostMapping("/getRole")
    public BaseResponse<Page<UserRoleVO>> getRole(@RequestBody UserRoleQueryRequest request) {
        Page<UserRoleVO> userRoleVOList = userRoleService.getUserRoleVOList(request);
        return new BaseResponse<>(userRoleVOList);
    }

    /**
     * 删除角色信息
     */
    @GetMapping("/delete")
    public BaseResponse<Integer> deleteRole(@RequestParam("roleId") int roleId) {
        return new BaseResponse<>(userRoleService.deleteUserRolesByRoleId(roleId));
    }
}

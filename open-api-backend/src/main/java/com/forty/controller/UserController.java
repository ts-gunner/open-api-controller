package com.forty.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.forty.common.BaseResponse;
import com.forty.common.CodeStatus;
import com.forty.exception.BusinessException;
import com.forty.model.dto.user.*;
import com.forty.model.entity.TokenData;
import com.forty.model.entity.UserInfo;
import com.forty.model.vo.LoginUserVO;
import com.forty.model.vo.UserVO;
import com.forty.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    @Operation(description = "用户注册， 返回用户注册成功后的用户ID")
    public BaseResponse<Long> register(@RequestBody UserRegisterRequest request) {
        if (request == null){
            throw new BusinessException(CodeStatus.PARAM_ERROR);
        }
        String userAccount = request.getUserAccount();
        String password = request.getPassword();
        String checkPassword = request.getCheckPassword();
        Long result = userService.userRegister(userAccount, password, checkPassword);
        return new BaseResponse<>(result);
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    @Operation(description = "用户登录， 返回token")
    public BaseResponse<String> userLogin(@RequestBody UserLoginRequest request){
        if (request == null) throw new BusinessException(CodeStatus.PARAM_ERROR);
        String userAccount = request.getUserAccount();
        String password = request.getPassword();
        String token = userService.userLogin(userAccount, password);
        return new BaseResponse<>(token);
    }

    /**
     * 获取当前登录用户
     */
    @GetMapping("/get/user_info")
    @Operation(description = "获取当前登录用户的用户信息")
    public BaseResponse<LoginUserVO> getUserInfo(@RequestAttribute("tokenData") TokenData tokenData){
        UserInfo user = userService.getUserByUserId(tokenData.getUserId());
        return new BaseResponse<>(userService.getLoginUserVO(user, tokenData));
    }

    /**
     * 用户的增删改查
     */
    @PostMapping("/add/user_info")
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest request){
        if (request == null) throw new BusinessException(CodeStatus.PARAM_ERROR);
        return new BaseResponse<>(userService.addUserData(request));
    }

    /**
     * 获取用户列表
     */
    @PostMapping("/get/user_list")
    @Operation(description = "获取用户列表， 仅限admin")
    public BaseResponse<Page<UserVO>> getUserList(@RequestBody UserQueryRequest request){
        Page<UserVO> userVOList = userService.getUserVOList(request);
        return new BaseResponse<>(userVOList);
    }

    /**
     * 删除用户
     */
    @GetMapping("/delete/user_info")
    @Operation(description = "删除用户")
    public BaseResponse<String> deleteUser(
            @RequestAttribute("tokenData") TokenData tokenData,
            @RequestParam("userId") Long userId
            ){
        if (userId == null) throw new BusinessException(CodeStatus.PARAM_ERROR);
        if (userId.equals(tokenData.getUserId())) throw new BusinessException(CodeStatus.PARAM_ERROR, "不可以删除自己的账号");
        userService.deleteUserData(userId);
        return new BaseResponse<>();
    }

    /**
     * 更新用户部分信息
     */
    @PostMapping("/update/user_info")
    @Operation(description = "更新用户信息")
    public BaseResponse<Integer> updateUser(@RequestBody UserUpdateRequest request){
        return new BaseResponse<>(userService.updateUserData(request));
    }

    /**
     * 重置用户密码
     */
    @PostMapping("/reset/password")
    public BaseResponse<Integer> resetPassword(){
        return new BaseResponse<>();
    }

    /**
     * 新建密钥key
     */
    @GetMapping("/create_user_secret")
    public BaseResponse<String> createUserSecret(@RequestAttribute("tokenData") TokenData tokenData){
        String secretKey = userService.createSecretKey(tokenData.getUserId());
        return new BaseResponse<>(secretKey);
    }

}

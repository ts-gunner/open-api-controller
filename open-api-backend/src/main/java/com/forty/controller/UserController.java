package com.forty.controller;

import com.forty.common.BaseResponse;
import com.forty.common.CodeStatus;
import com.forty.exception.BusinessException;
import com.forty.model.dto.user.UserLoginRequest;
import com.forty.model.dto.user.UserRegisterRequest;
import com.forty.model.entity.TokenData;
import com.forty.model.entity.UserInfo;
import com.forty.model.vo.LoginUserVO;
import com.forty.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@Tag(name="用户信息的curd")
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
        return new BaseResponse<>(userService.getLoginUserVO(user));
    }
}

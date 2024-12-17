package com.forty.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.forty.model.dto.user.UserAddRequest;
import com.forty.model.dto.user.UserQueryRequest;
import com.forty.model.dto.user.UserUpdateRequest;
import com.forty.model.entity.TokenData;
import com.forty.model.entity.UserInfo;
import com.forty.model.vo.LoginUserVO;
import com.forty.model.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.catalina.User;

public interface UserService extends IService<UserInfo> {

    Long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     *
     * @param userAccount 用户账号
     * @param userPassword 密码
     * @return token
     */
    String userLogin(String userAccount, String userPassword, HttpServletRequest request);

    UserInfo getUserByUserId(Long userId);

    LoginUserVO getLoginUserVO(UserInfo userInfo, TokenData tokenData);

    Page<UserVO> getUserVOList(UserQueryRequest request);

    long addUserData(UserAddRequest userAddRequest);

    void deleteUserData(Long userId);

    int updateUserData(UserUpdateRequest request);

    String createSecretKey(Long userId);
}

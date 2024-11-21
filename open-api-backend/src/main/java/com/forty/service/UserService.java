package com.forty.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.forty.model.entity.UserInfo;
import com.forty.model.vo.LoginUserVO;
import org.apache.catalina.User;

public interface UserService extends IService<UserInfo> {

    Long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     *
     * @param userAccount 用户账号
     * @param userPassword 密码
     * @return token
     */
    String userLogin(String userAccount, String userPassword);

    UserInfo getUserByUserId(Long userId);

    LoginUserVO getLoginUserVO(UserInfo userInfo);

}

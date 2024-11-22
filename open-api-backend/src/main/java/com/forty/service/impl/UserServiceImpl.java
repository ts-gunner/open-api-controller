package com.forty.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.forty.common.CodeStatus;
import com.forty.config.Settings;
import com.forty.constant.CommonConstant;
import com.forty.exception.BusinessException;
import com.forty.mapper.UserInfoMapper;
import com.forty.model.dto.user.UserAddRequest;
import com.forty.model.dto.user.UserQueryRequest;
import com.forty.model.entity.UserInfo;
import com.forty.model.entity.TokenData;
import com.forty.model.vo.LoginUserVO;
import com.forty.model.vo.UserVO;
import com.forty.service.UserService;
import com.forty.utils.JWTUtils;
import jakarta.annotation.Resource;
import org.apache.catalina.User;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.service.RequestBodyService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
        implements UserService {

    @Resource
    Settings settings;
    private RequestBodyService requestBodyBuilder;

    public Long userRegister(String userAccount, String userPassword, String checkPassword) {
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(CodeStatus.PARAM_ERROR, "参数不能为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(CodeStatus.PARAM_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 6 || checkPassword.length() < 6) {
            throw new BusinessException(CodeStatus.PARAM_ERROR, "用户密码过短");
        }
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(CodeStatus.PARAM_ERROR, "两次输入的密码不一致");
        }
        // 并发时访问userAccount就会被锁住
        synchronized (userAccount.intern()) {
            QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_account", userAccount);
            Long count = this.baseMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException(CodeStatus.PARAM_ERROR, "账号已存在");
            }
            // 加密
            String encryptPassword = DigestUtils.md5DigestAsHex((settings.getSalt() + userPassword).getBytes());
            // 插入数据
            UserInfo userInfo = new UserInfo();
            userInfo.setUserAccount(userAccount);
            userInfo.setPassword(encryptPassword);
            boolean saveResult = this.save(userInfo);
            if (!saveResult) {
                throw new BusinessException(CodeStatus.SYSTEM_ERROR, "注册失败, 数据库异常");
            }
            return userInfo.getId();
        }
    }

    @Override
    public String userLogin(String userAccount, String userPassword) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        UserInfo userInfo = this.baseMapper.selectOne(queryWrapper);
        if (userInfo == null) throw new BusinessException(CodeStatus.NO_AUTH, "用户名或密码错误");
        String encryptPassword = DigestUtils.md5DigestAsHex((settings.getSalt() + userPassword).getBytes());
        if (!encryptPassword.equals(userInfo.getPassword()))
            throw new BusinessException(CodeStatus.NO_AUTH, "用户名或密码错误");

        TokenData tokenData = new TokenData();
        tokenData.setUserName(userInfo.getUserName());
        tokenData.setUserId(userInfo.getId());
        Map map = JSON.parseObject(JSON.toJSONString(tokenData), Map.class);
        return JWTUtils.encrypt(map, settings.getSecretKey());
    }

    @Override
    public UserInfo getUserByUserId(Long userId) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        UserInfo userInfo = this.baseMapper.selectOne(queryWrapper);
        if (userInfo == null) throw new BusinessException(CodeStatus.DATA_NOT_EXIST, "找不到该用户");
        return userInfo;
    }

    @Override
    public LoginUserVO getLoginUserVO(UserInfo userInfo) {
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(userInfo, loginUserVO);
        return loginUserVO;
    }

    @Override
    public Page<UserVO> getUserVOList(UserQueryRequest request) {
        QueryWrapper<UserInfo> userInfoQueryWrapper = this.getUserInfoQueryWrapper(request);
        int currentPage = request.getCurrentPage();
        int pageSize = request.getPageSize();
        Page<UserInfo> userInfoPage = this.baseMapper.selectPage(new Page<>(currentPage, pageSize), userInfoQueryWrapper);
        Page<UserVO> userVOPage = new Page<>(userInfoPage.getCurrent(), userInfoPage.getSize(), userInfoPage.getTotal());
        List<UserVO> list = userInfoPage.getRecords().stream().map((record) -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(record, userVO);
            return userVO;
        }).toList();
        userVOPage.setRecords(list);
        return userVOPage;
    }

    @Override
    public long addUserData(UserAddRequest userAddRequest) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAddRequest.getUserAccount());
        UserInfo user = this.baseMapper.selectOne(queryWrapper);
        if (user != null) throw new BusinessException(CodeStatus.FAIL, "用户已存在");

        UserInfo userInfo = new UserInfo();
        userInfo.setUserAccount(userAddRequest.getUserAccount());
        String encryptPassword = DigestUtils.md5DigestAsHex((settings.getSalt() + userAddRequest.getPassword()).getBytes());
        userInfo.setPassword(encryptPassword);
        userInfo.setUserName(userAddRequest.getUsername());
        userInfo.setUserProfile(userAddRequest.getUserProfile());
        boolean result = this.save(userInfo);
        if (!result) throw new BusinessException(CodeStatus.DB_ERROR, "数据库写入失败");

        return userInfo.getId();
    }

    @Override
    public void deleteUserData(Long userId) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        UserInfo userInfo = this.baseMapper.selectOne(queryWrapper);
        if (userInfo == null) throw new BusinessException(CodeStatus.DATA_NOT_EXIST, "找不到该用户");
        this.baseMapper.deleteById(userInfo);
    }

    public QueryWrapper<UserInfo> getUserInfoQueryWrapper(UserQueryRequest request) {
        Long id = request.getId();
        String userName = request.getUserName();
        String userAccount = request.getUserAccount();
        String sortField = request.getSortField();
        String sortOrder = request.getSortOrder();
        String unionId = request.getUnionId();
        String mpOpenId = request.getMpOpenId();
        String userProfile = request.getUserProfile();

        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(!StringUtils.isBlank(userAccount), "user_account", userAccount);
        queryWrapper.like(!StringUtils.isBlank(userName), "user_name", userName);
        queryWrapper.eq(!StringUtils.isBlank(unionId), "union_id", unionId);
        queryWrapper.eq(!StringUtils.isBlank(mpOpenId), "mp_open_id", mpOpenId);
        queryWrapper.like(!StringUtils.isBlank(userProfile), "user_profile", userProfile);

        queryWrapper.orderBy(
                !StringUtils.isBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField
        );
        return queryWrapper;

    }
}

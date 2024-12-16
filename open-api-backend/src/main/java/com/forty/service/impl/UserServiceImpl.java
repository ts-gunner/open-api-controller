package com.forty.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.forty.common.CodeStatus;
import com.forty.config.Settings;
import com.forty.constant.CommonConstant;
import com.forty.exception.BusinessException;
import com.forty.mapper.RoleAssignmentMapper;
import com.forty.mapper.SecretInfoMapper;
import com.forty.mapper.UserInfoMapper;
import com.forty.model.dto.roleassignment.RoleAssignmentQueryRequest;
import com.forty.model.dto.user.UserAddRequest;
import com.forty.model.dto.user.UserQueryRequest;
import com.forty.model.dto.user.UserUpdateRequest;
import com.forty.model.entity.RoleAssignment;
import com.forty.model.entity.SecretInfo;
import com.forty.model.entity.UserInfo;
import com.forty.model.entity.TokenData;
import com.forty.model.vo.LoginUserVO;
import com.forty.model.vo.RoleAssignmentVO;
import com.forty.model.vo.UserVO;
import com.forty.service.RoleAssignmentService;
import com.forty.service.UserService;
import com.forty.utils.EncryptUtils;
import com.forty.utils.JWTUtils;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
        implements UserService {

    @Resource
    Settings settings;

    @Resource
    RoleAssignmentService roleAssignmentService;

    @Resource
    RoleAssignmentMapper roleAssignmentMapper;

    @Resource
    SecretInfoMapper secretInfoMapper;

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
            userInfo.setSecretId(EncryptUtils.generateEncryptString(settings.getSalt() + userAccount));
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
        // 获取用户角色
        RoleAssignmentQueryRequest request = new RoleAssignmentQueryRequest();
        request.setUserAccount(userAccount);
        Page<RoleAssignmentVO> userRoleMapVOList = roleAssignmentService.getUserRoleMapVOList(request);
        List<String> roles = userRoleMapVOList.getRecords().stream().map(RoleAssignmentVO::getRoleName).toList();
        TokenData tokenData = new TokenData();
        tokenData.setUserAccount(userInfo.getUserAccount());
        tokenData.setUserId(userInfo.getId());
        tokenData.setRoles(roles);
        Map map = JSON.parseObject(JSON.toJSONString(tokenData), Map.class);
        return JWTUtils.encryptPersistent(map, settings.getSecretKey());
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
    public LoginUserVO getLoginUserVO(UserInfo userInfo, TokenData tokenData) {
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(userInfo, loginUserVO);
        loginUserVO.setRoles(tokenData.getRoles());
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
            List<RoleAssignmentVO> roleAssignmentList = roleAssignmentMapper.getRoleAssignmentList(record.getUserAccount(), null, null);
            List<String> roles = roleAssignmentList.stream().map(RoleAssignmentVO::getRoleName).toList();
            userVO.setRoles(roles);
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
        userInfo.setSecretId(EncryptUtils.generateEncryptString(settings.getSalt() + userAddRequest.getUserAccount()));
        userInfo.setUserProfile(userAddRequest.getUserProfile());
        boolean result = this.save(userInfo);
        if (!result) throw new BusinessException(CodeStatus.DB_ERROR, "数据库写入失败");

        return userInfo.getId();
    }

    @Override
    public void deleteUserData(Long userId) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        this.baseMapper.delete(queryWrapper);
        // 删除对应的角色映射表
        QueryWrapper<RoleAssignment> roleAssignmentQueryWrapper = new QueryWrapper<>();
        roleAssignmentQueryWrapper.eq("user_id", userId);
        this.roleAssignmentMapper.delete(roleAssignmentQueryWrapper);
    }

    @Override
    public int updateUserData(UserUpdateRequest request) {
        UpdateWrapper<UserInfo> wrapper = new UpdateWrapper<>();
        wrapper.eq("id",request.getId());
        wrapper.set(!StringUtils.isBlank(request.getUserName()), "user_name",request.getUserName());
        wrapper.set(!StringUtils.isBlank(request.getUserProfile()), "user_profile",request.getUserProfile());
        return this.baseMapper.update(wrapper);
    }

    @Override
    public String createSecretKey(Long userId) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        UserInfo userInfo = this.baseMapper.selectOne(queryWrapper);
        if (userInfo == null) throw new BusinessException(CodeStatus.DATA_NOT_EXIST, "找不到该用户");
        UpdateWrapper<SecretInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", userId);
        updateWrapper.eq("available", true);
        updateWrapper.set("available", false);
        secretInfoMapper.update(updateWrapper);
        String secretKey = EncryptUtils.generateRandomSecret(40);
        SecretInfo secretInfo = new SecretInfo();
        secretInfo.setUserId(userId);
        secretInfo.setSecretId(userInfo.getSecretId());
        secretInfo.setSecretKey(secretKey);
        int insertResult = secretInfoMapper.insert(secretInfo);
        if (insertResult == 0) throw new BusinessException(CodeStatus.DB_ERROR);
        return secretKey;
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

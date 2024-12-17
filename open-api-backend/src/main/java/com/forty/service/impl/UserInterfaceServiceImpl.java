package com.forty.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.forty.mapper.InterfaceInfoMapper;
import com.forty.mapper.UserInterfaceInfoMapper;
import com.forty.model.dto.userinterfaceinfo.UserInterfaceQueryRequest;
import com.forty.model.entity.InterfaceInfo;
import com.forty.model.entity.TokenData;
import com.forty.model.entity.UserInterfaceInfo;
import com.forty.model.vo.InterfaceInfoVO;
import com.forty.model.vo.UserInterfaceVO;
import com.forty.service.UserInterfaceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserInterfaceServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo> implements UserInterfaceService {

    @Autowired
    UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Autowired
    InterfaceInfoMapper interfaceInfoMapper;

    @Override
    public Page<UserInterfaceVO> getUserInterfaceInfoList(UserInterfaceQueryRequest request) {
        // 获取所有接口的情况
        Page<InterfaceInfo> page = new Page<>(request.getCurrentPage(), request.getPageSize());
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", true);
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoMapper.selectPage(page, queryWrapper);
        // 获取这些接口的id
        List<Integer> interfaceIds = interfaceInfoPage.getRecords().stream().map(InterfaceInfo::getId).toList();

        // 根据用户id和interfaceId去查调用次数
        QueryWrapper<UserInterfaceVO> userInterfaceInfoQueryWrapper = new QueryWrapper<>();
        userInterfaceInfoQueryWrapper.eq("user_id", request.getUserId());
        userInterfaceInfoQueryWrapper.in("interface_id", interfaceIds);
        List<UserInterfaceVO> userInterfaceVOList = userInterfaceInfoMapper.getUserInterfaceVOList(userInterfaceInfoQueryWrapper);

        List<UserInterfaceVO> userInterfaceVOList1 = interfaceInfoPage.getRecords().stream().map(interfaceInfo -> {
            UserInterfaceVO userInterfaceVO = new UserInterfaceVO();
            BeanUtils.copyProperties(interfaceInfo, userInterfaceVO);
            // 设置接口id
            userInterfaceVO.setInterfaceId(interfaceInfo.getId());
            // 查找对应的调用次数记录
            Optional<UserInterfaceVO> filterUserInterfaceVO = userInterfaceVOList.stream().filter(item -> item.getInterfaceId().equals(interfaceInfo.getId())).findFirst();
            if (filterUserInterfaceVO.isPresent()) {
                UserInterfaceVO userInterfaceVO1 = filterUserInterfaceVO.get();
                userInterfaceVO.setTotalCalls(userInterfaceVO1.getTotalCalls());
                userInterfaceVO.setTotalCount(userInterfaceVO1.getTotalCount());
                userInterfaceVO.setRemainCount(userInterfaceVO1.getRemainCount());
            } else {
                userInterfaceVO.setTotalCalls(0L);
                userInterfaceVO.setTotalCount(0);
                userInterfaceVO.setRemainCount(0);
            }
            return userInterfaceVO;
        }).toList();

        Page<UserInterfaceVO> userInterfaceVOPage = new Page<>(request.getCurrentPage(), request.getPageSize());
        userInterfaceVOPage.setTotal(interfaceInfoPage.getTotal());
        userInterfaceVOPage.setRecords(userInterfaceVOList1);
        return userInterfaceVOPage;
    }

    @Override
    public Page<UserInterfaceVO> getUserInterfaceInfoList(UserInterfaceQueryRequest request, TokenData tokenData) {
        // 获取所有接口的情况
        Page<InterfaceInfo> page = new Page<>(request.getCurrentPage(), request.getPageSize());
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", true);
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoMapper.selectPage(page, queryWrapper);
        // 获取这些接口的id
        List<Integer> interfaceIds = interfaceInfoPage.getRecords().stream().map(InterfaceInfo::getId).toList();

        // 根据用户id和interfaceId去查调用次数
        QueryWrapper<UserInterfaceVO> userInterfaceInfoQueryWrapper = new QueryWrapper<>();
        userInterfaceInfoQueryWrapper.eq("user_id", tokenData.getUserId());
        userInterfaceInfoQueryWrapper.in("interface_id", interfaceIds);
        List<UserInterfaceVO> userInterfaceVOList = userInterfaceInfoMapper.getUserInterfaceVOList(userInterfaceInfoQueryWrapper);

        List<UserInterfaceVO> userInterfaceVOList1 = interfaceInfoPage.getRecords().stream().map(interfaceInfo -> {
            UserInterfaceVO userInterfaceVO = new UserInterfaceVO();
            BeanUtils.copyProperties(interfaceInfo, userInterfaceVO);
            userInterfaceVO.setInterfaceId(interfaceInfo.getId());
            // 查找对应的调用次数记录
            Optional<UserInterfaceVO> filterUserInterfaceVO = userInterfaceVOList.stream().filter(item -> item.getInterfaceId().equals(interfaceInfo.getId())).findFirst();
            if (filterUserInterfaceVO.isPresent()) {
                UserInterfaceVO userInterfaceVO1 = filterUserInterfaceVO.get();
                userInterfaceVO.setTotalCalls(interfaceInfo.getTotalCalls());
                userInterfaceVO.setTotalCount(userInterfaceVO1.getTotalCount());
                userInterfaceVO.setRemainCount(userInterfaceVO1.getRemainCount());
            } else {
                userInterfaceVO.setTotalCalls(interfaceInfo.getTotalCalls());
                userInterfaceVO.setTotalCount(0);
                userInterfaceVO.setRemainCount(0);
            }
            return userInterfaceVO;
        }).toList();

        Page<UserInterfaceVO> userInterfaceVOPage = new Page<>(request.getCurrentPage(), request.getPageSize());
        userInterfaceVOPage.setTotal(interfaceInfoPage.getTotal());
        userInterfaceVOPage.setRecords(userInterfaceVOList1);
        return userInterfaceVOPage;

    }
}

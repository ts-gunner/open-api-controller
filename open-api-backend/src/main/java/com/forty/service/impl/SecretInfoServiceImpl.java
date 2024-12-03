package com.forty.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.forty.mapper.SecretInfoMapper;
import com.forty.model.entity.SecretInfo;
import com.forty.service.SecretInfoService;
import org.springframework.stereotype.Service;

@Service
public class SecretInfoServiceImpl extends ServiceImpl<SecretInfoMapper, SecretInfo> implements SecretInfoService {
    @Override
    public SecretInfo getSecretObject(Long userId) {
        QueryWrapper<SecretInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("available", true);
        return this.baseMapper.selectOne(queryWrapper);
    }
}

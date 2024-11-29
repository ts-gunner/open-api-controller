package com.forty.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.forty.mapper.SecretInfoMapper;
import com.forty.model.SecretInfo;
import com.forty.service.SecretService;
import org.springframework.stereotype.Service;


@Service
public class SecretServiceImpl extends ServiceImpl<SecretInfoMapper, SecretInfo> implements SecretService {


    @Override
    public SecretInfo getSecretInfo(String secretId) {
        QueryWrapper<SecretInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("secret_id", secretId);
        queryWrapper.eq("available", true);
        return this.baseMapper.selectOne(queryWrapper);
    }
}
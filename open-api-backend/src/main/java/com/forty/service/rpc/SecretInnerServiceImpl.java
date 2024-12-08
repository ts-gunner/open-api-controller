package com.forty.service.rpc;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.forty.mapper.SecretInfoMapper;
import com.forty.model.entity.SecretInfo;
import com.forty.remote.SecretInnerService;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;


@DubboService
public class SecretInnerServiceImpl implements SecretInnerService {

    @Resource
    SecretInfoMapper secretInfoMapper;


    @Override
    public SecretInfo getSecretInfo(String secretId) {
        QueryWrapper<SecretInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("secret_id", secretId);
        queryWrapper.eq("available", true);
        return secretInfoMapper.selectOne(queryWrapper);
    }
}

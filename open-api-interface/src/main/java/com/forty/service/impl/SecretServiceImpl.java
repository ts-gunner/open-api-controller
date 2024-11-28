package com.forty.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.forty.mapper.SecretInfoMapper;
import com.forty.model.SecretInfo;
import com.forty.service.SecretService;
import org.springframework.stereotype.Service;


@Service
public class SecretServiceImpl extends ServiceImpl<SecretInfoMapper, SecretInfo> implements SecretService {



}
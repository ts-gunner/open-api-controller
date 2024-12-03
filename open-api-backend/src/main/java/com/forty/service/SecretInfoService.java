package com.forty.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.forty.model.entity.SecretInfo;

public interface SecretInfoService extends IService<SecretInfo> {

    SecretInfo getSecretObject(Long userId);
}

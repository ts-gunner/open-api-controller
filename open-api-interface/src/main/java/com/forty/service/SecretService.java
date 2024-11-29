package com.forty.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.forty.model.SecretInfo;

public interface SecretService extends IService<SecretInfo> {
    SecretInfo getSecretInfo(String secretId);
}

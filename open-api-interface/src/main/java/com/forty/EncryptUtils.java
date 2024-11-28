package com.forty;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

public class EncryptUtils {

    public static String encryptBySha256(String body){
        Digester md5 = new Digester(DigestAlgorithm.SHA256);
        return md5.digestHex(body);
    }
}

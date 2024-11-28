package com.forty.utils;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

public class EncryptUtils {
    public static String generateEncryptString(String body) {
        Digester md5 = new Digester(DigestAlgorithm.SHA256);
        return md5.digestHex(body);
    }

    public static String generateRandomSecret(int length) {
        String customChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                "abcdefghijklmnopqrstuvwxyz" +
                "0123456789" +
                "!@#$%^&*()-_=+[]{}|;:,.<>?";
        return RandomUtil.randomString(customChars, length);
    }

}

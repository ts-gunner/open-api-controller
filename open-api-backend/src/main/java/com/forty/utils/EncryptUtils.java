package com.forty.utils;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

import java.security.SecureRandom;
import java.util.Arrays;

public class EncryptUtils {

    public static String generateEncryptId(String salt) {
        Digester md5 = new Digester(DigestAlgorithm.SHA256);
        return md5.digestHex(salt);
    }

    public static String generateRandomSecret(int length) {
        String customChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                "abcdefghijklmnopqrstuvwxyz" +
                "0123456789" +
                "!@#$%^&*()-_=+[]{}|;:,.<>?";
        return RandomUtil.randomString(customChars, length);
    }
}

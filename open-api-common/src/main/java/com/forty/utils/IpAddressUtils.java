package com.forty.utils;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

public class IpAddressUtils {
    public static boolean isIpAddressInSubnet(String ipAddress, String subnetAddress, String subnetMask) throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getByName(ipAddress);
        InetAddress subnet = InetAddress.getByName(subnetAddress);
        InetAddress mask = InetAddress.getByName(subnetMask);

        byte[] inetAddressBytes = inetAddress.getAddress();
        byte[] subnetBytes = subnet.getAddress();
        byte[] maskBytes = mask.getAddress();

        for (int i = 0; i < inetAddressBytes.length; i++) {
            int addressByte = inetAddressBytes[i] & 0xFF;
            int subnetByte = subnetBytes[i] & 0xFF;
            int maskByte = maskBytes[i] & 0xFF;

            if ((addressByte & maskByte) != (subnetByte & maskByte)) {
                return false;
            }
        }

        return true;
    }


    /**
     * 判断是否在该网段中
     *
     * @param subnetRange 子网范围 x.x.x.x/xx 形式
     */
    public static boolean isIpAddressInSubnet(String ipAddress, String subnetRange) {
        String[] networkips = ipAddress.split("\\.");
        int ipAddr = (Integer.parseInt(networkips[0]) << 24)
                | (Integer.parseInt(networkips[1]) << 16)
                | (Integer.parseInt(networkips[2]) << 8)
                | Integer.parseInt(networkips[3]);

        // 拿到主机数
        int type = Integer.parseInt(subnetRange.replaceAll(".*/", ""));
        int ipCount = 0xFFFFFFFF << (32 - type);

        String maskIp = subnetRange.replaceAll("/.*", "");
        String[] maskIps = maskIp.split("\\.");

        int cidrIpAddr = (Integer.parseInt(maskIps[0]) << 24)
                | (Integer.parseInt(maskIps[1]) << 16)
                | (Integer.parseInt(maskIps[2]) << 8)
                | Integer.parseInt(maskIps[3]);

        return (ipAddr & ipCount) == (cidrIpAddr & ipCount);
    }

    /**
     * 如果ip是网段，则提取ip部分
     * @param subnet
     * @return
     */
    public static String extractIP(String subnet) {
        // 找到斜杠的位置并截取 IP 地址部分
        int slashIndex = subnet.indexOf('/');
        if (slashIndex != -1) {
            return subnet.substring(0, slashIndex);  // 返回斜杠前的部分
        } else {
            return subnet; // 如果没有斜杠，返回原字符串
        }
    }
    /**
     * 判断IP字符串是否合法
     */
    public static boolean isValidIP(String ip) {
        String IPV4_PATTERN =
                "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                        "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                        "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                        "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        return Pattern.matches(IPV4_PATTERN, extractIP(ip));
    }

    /**
     * 判断是否是网段
     */
    public static boolean isValidSubnet(String ip) {
        return ip.contains("/");
    }

}

package com.zhong.redisuv.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * ip地址工具类
 */
public class IpAddressUtil {

    private final static String[] HEADERS_TO_TRY = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR",
            "X-Real-IP"};

    private IpAddressUtil() {

    }

    /**
     * 获得ip
     *
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ipAddr = null;
        // 代理进来，则透过防火墙获取真实IP地址
        for (String header : HEADERS_TO_TRY) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                ipAddr = ip;
                break;
            }
        }
        // 如果没有代理，则获取真实ip
        if (ipAddr == null || ipAddr.length() == 0 || "unknown".equalsIgnoreCase(ipAddr)) {
            ipAddr = request.getRemoteAddr();
        }
        ipAddr = ipAddr.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ipAddr;
        return ipAddr;
    }

    /**
     * 隐藏ip地址
     *
     * @param ipAddress
     * @return
     */
    public static String hideIpAddress(String ipAddress) {
        String ip = ipAddress;
        // 注意点号分割要用转义字符
        String[] ips = ipAddress.split("\\.");
        if (ips.length == 4) {
            ip = ips[0] + "." + ips[1] + ".*.*";
        }
        return ip;
    }
}


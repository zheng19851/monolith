package com.runssnail.monolith.common.ip;

import sun.net.util.IPAddressUtil;

/**
 * ip工具
 * 
 * @author zhengwei
 */
public class IpAddressUtils {

    /**
     * ip4环回地址
     */
    private static final String DEFAULT_IP4_LOOPBACK  = "127.0.0.1";

    /**
     * ip6环回地址
     */
    private static final String DEFAULT_IP6_LOOPBACK1 = "0:0:0:0:0:0:0:1";

    /**
     * ip6环回地址
     */
    private static final String DEFAULT_IP6_LOOPBACK2 = "::1";

    private static final long   IP_BEGIN_10           = getIpNum("10.0.0.0");

    private static final long   IP_END_10             = getIpNum("10.255.255.255");

    private static final long   IP_BEGIN_172          = getIpNum("172.16.0.0");

    private static final long   IP_END_172            = getIpNum("172.31.255.255");

    private static final long   IP_BEGIN_192          = getIpNum("192.168.0.0");

    private static final long   IP_END_192            = getIpNum("192.168.255.255");

    private static final long   IP_1                  = 256 * 256 * 256;

    private static final long   IP_2                  = 256 * 256;

    private static final long   IP_3                  = 256;

    /**
     * 判断ip是否为内网ip 私有IP：A类 10.0.0.0-10.255.255.255 B类 172.16.0.0-172.31.255.255 C类 192.168.0.0-192.168.255.255
     * 当然，还有127这个网段是环回地址
     * 
     * @param ip
     * @return
     */
    public static boolean isInnerIP(String ip) {

        if (!isIPValid(ip)) {
            throw new RuntimeException("the ip address is not valid.");
        }

        if (DEFAULT_IP4_LOOPBACK.equals(ip) || DEFAULT_IP6_LOOPBACK1.equals(ip) || DEFAULT_IP6_LOOPBACK2.equals(ip)) {
            return true;
        }

        long ipNum = getIpNum(ip);

        return isInner(ipNum, IP_BEGIN_10, IP_END_10) || isInner(ipNum, IP_BEGIN_172, IP_END_172)
               || isInner(ipNum, IP_BEGIN_192, IP_END_192);
    }

    /**
     * 获取IP整数值
     */
    public static long getIpNum(String ipAddress) {
        String[] ip = ipAddress.split("\\.");
        long a = Integer.parseInt(ip[0]);
        long b = Integer.parseInt(ip[1]);
        long c = Integer.parseInt(ip[2]);
        long d = Integer.parseInt(ip[3]);

        return a * IP_1 + b * IP_2 + c * IP_3 + d;
    }

    private static boolean isInner(long userIp, long begin, long end) {
        return (userIp >= begin) && (userIp <= end);
    }

    /**
     * ip是否有效
     * 
     * @param ip
     * @return
     */
    public static boolean isIPValid(String ip) {
        return IPAddressUtil.isIPv4LiteralAddress(ip) || IPAddressUtil.isIPv6LiteralAddress(ip);
    }
}

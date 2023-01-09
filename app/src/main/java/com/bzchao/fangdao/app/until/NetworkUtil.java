package com.bzchao.fangdao.app.until;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class NetworkUtil {

    /**
     * Get Ip address 自动获取IP地址
     */
    public static List<String> getIpAddress() {
        List<String> hostIp = new ArrayList<>();
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ia;
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    ia = ias.nextElement();
                    String ip = ia.getHostAddress();
                    // 过滤掉127段的ip地址
                    if ("127.0.0.1".equals(ip)) {
                        continue;
                    }  // 过滤掉127段的ip地址
                    if ("::1".equals(ip)) {
                        continue;
                    }
                    hostIp.add(ip);
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return hostIp;
    }

}

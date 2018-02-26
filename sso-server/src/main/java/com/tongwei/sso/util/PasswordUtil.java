package com.tongwei.sso.util;

import com.tongwei.auth.util.AESUtil;

/**
 * @author yangz
 * @date 2018年2月3日 下午5:17:21
 * @description 密码工具类
 */
public class PasswordUtil {

    // 密码加密的盐值
    private static String encodePwdSalt = "yangz@919573416@qq.com";

    public static String encodePwd(String pwd) throws Exception {
        return AESUtil.encodeToHex(encodePwdSalt, pwd);
    }

    public static String decodePwd(String pwd) throws Exception {
        return AESUtil.decodeFromHex(encodePwdSalt, pwd);
    }

}

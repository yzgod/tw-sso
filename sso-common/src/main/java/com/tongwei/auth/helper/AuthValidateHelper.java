package com.tongwei.auth.helper;

import com.tongwei.auth.util.AuthUtil;

/**
 * @author yangz
 * @date 2018年2月7日 上午10:51:15
 * @description 权限认证帮助类
 */
public class AuthValidateHelper {

    public static boolean hasPermAny(String[] permGroups) {
        for (String str : permGroups) {
            String[] split = str.split(":");
            if (split.length != 2) {
                throw new IllegalArgumentException("value格式错误!,permGroupCode:permCode1,permCode2...");
            }
            String permGroup = split[0];
            String permsStr = split[1];
            String[] perms = permsStr.split(",");
            if (AuthUtil.hasPermsAny(permGroup, perms)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasPermAll(String[] permGroups) {
        for (String str : permGroups) {
            String[] split = str.split(":");
            if (split.length != 2) {
                throw new IllegalArgumentException("value格式错误!,permGroupCode:permCode1,permCode2...");
            }
            String permGroup = split[0];
            String permsStr = split[1];
            String[] perms = permsStr.split(",");
            if (!AuthUtil.hasPermsAll(permGroup, perms)) {
                return false;
            }
        }
        return true;
    }

}

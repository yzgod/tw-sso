package com.tongwei.sso.util;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.pagehelper.util.StringUtil;
import com.tongwei.auth.model.Role;

/**
 * @author yangz
 * @date 2018年1月31日 上午10:16:06
 * @description 辅助工具类
 */
public class AppUtils {
    
    private static final Pattern CODE_PATTERN = Pattern.compile("[0-9A-Za-z_]*");
    
    public static boolean validateCode(String code) {
        if(StringUtil.isEmpty(code)){
            return false;
        }
        Matcher matcher = CODE_PATTERN.matcher(code);
        return matcher.matches();
    }

    /**
     * 递归处理角色
     */
    public static void recursionRoles(HashSet<Role> cleanRoles, Role role) {
        Role parentRole = role.getParentRole();
        if (parentRole == null) {
            cleanRoles.add(role);
        } else {
            role.setParentId(null);
            role.setParentRole(null);
            cleanRoles.add(role);
            recursionRoles(cleanRoles, parentRole);
        }
    }

}

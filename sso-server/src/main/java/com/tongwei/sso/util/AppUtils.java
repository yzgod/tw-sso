package com.tongwei.sso.util;

import java.util.HashSet;

import com.tongwei.auth.model.Role;

/**
 * @author		yangz
 * @date		2018年1月31日 上午10:16:06
 * @description	
 */
public class AppUtils {
	
	/**
	 *递归处理角色
	 */
	public static void recursionRoles(HashSet<Role> cleanRoles, Role role) {
		Role parentRole = role.getParentRole();
		if(parentRole==null){
			cleanRoles.add(role);
		}else{
			role.setParentId(null);
			role.setParentRole(null);
			cleanRoles.add(role);
			recursionRoles(cleanRoles, parentRole);
		}
	}

}

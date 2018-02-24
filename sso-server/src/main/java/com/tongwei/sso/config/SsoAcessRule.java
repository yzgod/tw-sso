package com.tongwei.sso.config;

import java.util.Set;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.tongwei.auth.model.AuthUser;
import com.tongwei.auth.model.Role;
import com.tongwei.auth.security.rule.LoginAccessRule;
import com.tongwei.auth.util.AuthUtil;
import com.tongwei.auth.util.LogUtil;

/**
 * @author		yangz
 * @date		2018年2月9日 上午11:48:22
 * @description	SSO访问非anno地址的规则
 * <p> Primary
 */
@Component
@Primary
public class SsoAcessRule implements LoginAccessRule{

	@Override
	public boolean isAccess() {
		AuthUser authUser = AuthUtil.getAuthUser();
		Set<Role> roles = authUser.getRoles();
		if(roles==null || roles.isEmpty()){
			LogUtil.logToDbOp("access denied");
			return false;
		}
		for (Role role : roles) {
			if(role.getCode().startsWith("system")){
				return true;
			}
		}
		LogUtil.logToDbOp("access denied");
		return false;
	}

}

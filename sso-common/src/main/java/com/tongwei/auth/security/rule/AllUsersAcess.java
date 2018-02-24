package com.tongwei.auth.security.rule;

/**
 * @author		yangz
 * @date		2018年2月9日 上午11:48:22
 * @description	默认登录后即可访问非anno地址
 */
public class AllUsersAcess implements LoginAccessRule{

	@Override
	public boolean isAccess() {
		return true;
	}

}

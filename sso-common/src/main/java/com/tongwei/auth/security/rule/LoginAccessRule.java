package com.tongwei.auth.security.rule;

/**
 * @author		yangz
 * @date		2018年2月9日 上午11:38:30
 * @description	能访问系统的规则接口
 * <p>isAccess  内可以调用AuthUtil自定义验证规则
 */
public interface LoginAccessRule {

	boolean isAccess();
	
}

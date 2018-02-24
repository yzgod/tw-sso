package com.tongwei.auth.security.rule;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tongwei.auth.security.RememberMeType;

/**
 * @author		yangz
 * @date		2018年1月25日 下午6:03:27
 * @description	记住登录验证安全规则
 */
public interface RememberMeRule {
	
	/**
	 * 生成值
	 */
	String generateValue(HttpServletRequest request,RememberMeType type,Integer userId);
	
	
	/**
	 * 获取值
	 */
	String findGenerateValue(HttpServletRequest request);
	
	/**
	 * 验证值
	 */
	boolean validate(HttpServletRequest request, HttpServletResponse response, FilterChain chain,RememberMeType type,int rememberMeExpireTime) ;
	
}

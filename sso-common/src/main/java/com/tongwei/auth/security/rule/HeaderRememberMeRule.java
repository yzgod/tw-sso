package com.tongwei.auth.security.rule;

import javax.servlet.http.HttpServletRequest;

import com.tongwei.Const;

/**
 * @author		yangz
 * @date		2018年2月9日 下午2:31:58
 * @description	Header策略的记住规则
 */
public class HeaderRememberMeRule extends AbstractRememberMeRule {

	@Override
	public String findGenerateValue(HttpServletRequest request) {
		return request.getHeader(Const.AUTHUSER_REMEMBERME);
	}

}

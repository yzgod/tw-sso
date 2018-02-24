package com.tongwei.auth.security.rule;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.tongwei.Const;

/**
 * @author		yangz
 * @date		2018年2月9日 下午2:31:58
 * @description	Cookie策略的记住规则
 */
public class CookieRememberMeRule extends AbstractRememberMeRule {

	@Override
	public String findGenerateValue(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if(cookies!=null){
			for (Cookie cookie : cookies) {
				String name = cookie.getName();
				if(Const.AUTHUSER_REMEMBERME.equalsIgnoreCase(name)){
					return cookie.getValue().trim();
				}
			}
		}
		return null;
	}

}

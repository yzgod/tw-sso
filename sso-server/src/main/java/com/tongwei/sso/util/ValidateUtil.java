package com.tongwei.sso.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.pagehelper.util.StringUtil;

/**
 * @author		yangz
 * @date		2018年2月6日 上午11:17:03
 * @description	
 */
public class ValidateUtil {
	
	private static final Pattern CODE_PATTERN = Pattern.compile("[0-9A-Za-z_]*");
	
	public static boolean validateCode(String code) {
		if(StringUtil.isEmpty(code)){
			return false;
		}
		Matcher matcher = CODE_PATTERN.matcher(code);
        return matcher.matches();
    }
	

}

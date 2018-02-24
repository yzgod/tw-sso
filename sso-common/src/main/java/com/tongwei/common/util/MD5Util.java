package com.tongwei.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author		yangz
 * @date		2018年1月26日 上午9:59:50
 * @description	md5工具类
 */
public class MD5Util {
	
	public static String md5(String code){
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
		byte[] digest = md5.digest(code.getBytes());
		return byte2hex(digest);
	}
	
	private static String byte2hex(byte[] b) {
		StringBuilder sb = new StringBuilder();
		for (int n = 0; n < b.length; ++n) {
			String stmp = Integer.toHexString(b[n] & 0xFF);
			if (stmp.length() == 1) {
				sb.append("0");
			}
			sb.append(stmp);
		}
		return sb.toString().toUpperCase();
	}
	
}

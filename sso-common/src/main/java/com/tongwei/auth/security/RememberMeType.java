package com.tongwei.auth.security;

/**
 * @author		yangz
 * @date		2018年1月25日 下午7:15:18
 * @description	记住登录方式
 */
public enum RememberMeType {
	
	//远程主机地址加密
	HOST,
	//客户端
	USER_AGENT,
	//随机值
	NONE
}

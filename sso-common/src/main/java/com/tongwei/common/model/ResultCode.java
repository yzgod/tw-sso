package com.tongwei.common.model;

/**
 * @author		yangz
 * @date		2018年1月16日 上午10:50:21
 * @description	基础返回码，具体业务返回码可继承ResultCode
 */
public class ResultCode {

	public final static int SUCCESS 			= 200;// 成功
	public final static int ERROR 				= 500;// 未知错误
	
	public final static int APPLICATION_ERROR	= 8000;// 应用级错误
	public final static int AUTHENTICATION_ERROR= 8001;// 授权错误
	public final static int SERVICE_ERROR 		= 8002;// 业务逻辑验证错误
	public final static int CACHE_ERROR 		= 8003;// 缓存访问错误
	
	// SSO 用户授权出错
	public final static int SSO_TOKEN_ERROR 	= 1001; // TOKEN未授权或已过期
	public final static int SSO_PERMISSION_ERROR= 1002; // 没有访问权限
}

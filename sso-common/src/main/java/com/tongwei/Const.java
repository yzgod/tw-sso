package com.tongwei;

/**
 * @author yangz
 * @date 2018年1月16日 下午3:37:34
 * @description 全局常量
 */
public class Const {

	/** 用户ID存放session属性名 */
	public static final String SESSION_USER_ID = "SESSION_USER_ID";

	/** 用户REALNAME存放session属性名 */
	public static final String SESSION_USER_REALNAME = "SESSION_USER_REALNAME";

	/** 用户LOGINNAME存放session属性名 */
	public static final String SESSION_USER_LOGINNAME = "SESSION_USER_LOGINNAME";

	/** 用户CELLPHONE存放session属性名 */
	public static final String SESSION_USER_CELLPHONE = "SESSION_USER_CELLPHONE";

	/** authuser存放在请求域中的属性名称 */
	public static final String AUTH_USER_IN_REQUEST = "_AUTH_USER_IN_REQUEST_YZ_";

	/** rememberMe的authuser属性名 */
	public static final String AUTHUSER_REMEMBERME = "AUTHUSER";

	/** setcookie的地址 */
    public static final String SET_COOKIE_SERVLET_PATH = "/setcookie";

	// redis--key相关--------
	/** 用户存放REDIS的key前缀 */
	public static final String USER_PREFFIX = "sso:user:";

	/** 用户角色key中间名 全名appcode+ROLE_MIDDLE+roleId */
	public static final String ROLE_MIDDLE = ":role:";

	/** 用户角色key中间名 全名appcode+ROLE_MIDDLE+roleId */
	public static final String MENU_MIDDLE = ":menu:";

	/** AES加密的盐值key */
	public static final String AES_SALT = "sso:AES_SALT";

	public static final String QUEUE_USER_IDS_KEY = "queue:user:ids";

	public static final String QUEUE_REMEMBER_ME_KEY = "queue:rememberme:user:ids";

	public static final String QUEUE_ROLE_IDS_KEY = "queue:role:ids";

	public static final String QUEUE_LOG_KEY = "queue:sso:logs";

	// session管理相关--------
	// 用户与Session关系map
	public static final String MAP_ONLINE_USER_SESSION_IDS = "sso:MAP_ONLINE_USER_SESSION_IDS";

	// 默认值,不更改
	public static final String SPRING_SESSION_NAMESPACE = "spring:session:";

	// 过期key的前缀
	public static final String SPRING_SESSION_EXPIRE_PREFFIX = SPRING_SESSION_NAMESPACE + "sessions:expires:";

}

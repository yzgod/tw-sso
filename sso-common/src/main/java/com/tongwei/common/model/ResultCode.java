package com.tongwei.common.model;

/**
 * @author yangz
 * @date 2018年1月16日 上午10:50:21
 * @description 基础返回码，具体业务返回码可继承ResultCode
 */
public class ResultCode {

    public final static int SUCCESS = 200;// 成功
    public final static int ERROR = 500;// 未知错误

    public final static int APPLICATION_ERROR = 2000;// 应用级错误
    public final static int AUTHENTICATION_ERROR = 2010;// 授权错误
    public final static int CACHE_ERROR = 2020;// 缓存访问错误

    // SSO
    public final static int SSO_LOGIN_REJECTED_ERROR = 1010; // 禁止登录
    public final static int SSO_PERMISSION_ERROR = 1020; // 没有访问权限
}

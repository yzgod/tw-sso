package com.tongwei.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author yangz
 * @date 2018年2月3日 下午3:55:11
 * @description sso配置
 */
@ConfigurationProperties(prefix = "sso.sys")
public class SsoProperties {

    /** 单点登录地址 */
    private String loginUrl = "";

    /** 匿名的地址列表 */
    private String anno = "";

    /** 排除的静态资源后缀,分隔 */
    private String staticSuffixs = "";

    /** 是否启用菜单匹配 */
    private boolean enableMenuPattern;

    /** appCode */
    private String appCode;

    /** 登录成功后跳转地址 */
    private String successUrl = "";

    /** 是否启用记住登陆 */
    private boolean enableRememberMe;

    /** 记住登录后的过期时间秒 */
    private int rememberMeExpireTime = 7 * 24 * 3600;

    /** 应用是否属于认证中心 */
    private boolean isCenter;

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getAnno() {
        return anno;
    }

    public void setAnno(String anno) {
        this.anno = anno;
    }

    public String getStaticSuffixs() {
        return staticSuffixs;
    }

    public void setStaticSuffixs(String staticSuffixs) {
        this.staticSuffixs = staticSuffixs;
    }

    public boolean isEnableMenuPattern() {
        return enableMenuPattern;
    }

    public void setEnableMenuPattern(boolean enableMenuPattern) {
        this.enableMenuPattern = enableMenuPattern;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode.trim();
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public boolean isEnableRememberMe() {
        return enableRememberMe;
    }

    public void setEnableRememberMe(boolean enableRememberMe) {
        this.enableRememberMe = enableRememberMe;
    }

    public int getRememberMeExpireTime() {
        return rememberMeExpireTime;
    }

    public void setRememberMeExpireTime(int rememberMeExpireTime) {
        this.rememberMeExpireTime = rememberMeExpireTime;
    }

    public boolean isCenter() {
        return isCenter;
    }

    public void setIsCenter(boolean isCenter) {
        this.isCenter = isCenter;
    }

}

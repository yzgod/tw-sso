package com.tongwei.auth.log;

public class TextUserLogBean extends TextLogBean {

	private static final long serialVersionUID = 1L;
	
	public TextUserLogBean() {
	}
	
	public TextUserLogBean(String appCode, String msg, Throwable error, Class<?> clz, String th, String loginName) {
		super(appCode, msg, error, clz, th);
		this.loginName = loginName;
	}

	//操作人登陆名
	private String loginName;

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
}

package com.tongwei.auth.log;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author 		yangz
 * @date 		2018年2月3日 下午8:02:24
 * @description	持久化到mysql的日志,比如操作日志
 */
@Table(name="tp_log_access")
public class DBAccessUserLogBean extends DBUserLogBean {
	private static final long serialVersionUID = 1L;
	
	public DBAccessUserLogBean() {
	}

	public DBAccessUserLogBean(String appCode, String msg, String loginName) {
		super(appCode, msg, loginName);
	}
	
	public DBAccessUserLogBean(String appCode, String msg, String loginName, String method, String url, String ip,
			String parameter,Integer timeUsed) {
		super(appCode, msg, loginName);
		this.method = method;
		this.url = url;
		this.ip = ip;
		this.parameter = parameter;
		this.timeUsed = timeUsed;
	}

	//访问的方法类型get,post等
	private String method;
	//访问地址
	private String url;
	//远程ip地址
	private String ip;
	//请求参数
	private String parameter;
	@Column(name="time_used")
	private Integer timeUsed;

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public Integer getTimeUsed() {
		return timeUsed;
	}

	public void setTimeUsed(Integer timeUsed) {
		this.timeUsed = timeUsed;
	}

}

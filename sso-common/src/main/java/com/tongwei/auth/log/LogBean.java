package com.tongwei.auth.log;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Transient;

public class LogBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//应用编码
	@Column(name="app_code")
	protected String appCode;
	//发生时间
	@Column(name="create_date")
	protected Date date = new Date();
	//内容
	@Column(name="content")
	protected String msg;
	//类型 1-操作日志db  2-access访问详情日志db 11-文件日志nouser 12-文件日志user
	@Transient
	protected int type;
	
	public LogBean() {
		// TODO Auto-generated constructor stub
	}
	public LogBean(String appCode, String msg) {
		this.appCode = appCode;
		this.msg = msg;
	}
	
	public String getAppCode() {
		return appCode;
	}
	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
}

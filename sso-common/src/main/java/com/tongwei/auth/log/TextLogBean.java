package com.tongwei.auth.log;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author 		yangz
 * @date 		2018年2月3日 下午8:02:24
 * @description	普通日志的bean,此种日志将被存为文本文件
 */
public class TextLogBean extends LogBean{
	private static final long serialVersionUID = 1L;
	
	//异常
	@JSONField(deserialize=false,serialize=false)
	protected Throwable error;
	//报错的类
	protected Class<?> clz;
	//线程名
	protected String th;
	//errorMsg
	protected String errorMsg;
	
	public TextLogBean() {
	}
	
	public TextLogBean(String appCode, String msg, Throwable error, Class<?> clz, String th) {
		super(appCode, msg);
		this.error = error;
		this.clz = clz;
		this.th = th;
		this.errorMsg = printError(error);
	}

	public Throwable getError() {
		return error;
	}

	public void setError(Throwable error) {
		this.error = error;
		this.errorMsg = printError(error);
	}

	private String printError(Throwable error) {
		StringWriter stringWriter = new StringWriter();
	    error.printStackTrace(new PrintWriter(stringWriter));
		return stringWriter.toString();
	}

	public Class<?> getClz() {
		return clz;
	}

	public void setClz(Class<?> clz) {
		this.clz = clz;
	}

	public String getTh() {
		return th;
	}

	public void setTh(String th) {
		this.th = th;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
}

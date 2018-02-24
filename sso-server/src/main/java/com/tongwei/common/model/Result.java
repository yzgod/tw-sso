package com.tongwei.common.model;

import java.io.Serializable;

/**
 * @author 		yangz
 * @date 		2017年3月31日 下午8:57:48
 * @description 通用的返回结果
 */
public class Result implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	protected int code;			//服务器响应码
	protected String msg;		//响应消息
	protected Long total;		//请求数据的总记录数--包装类
	protected Object data;		//数据对象
	
	public Result() {
	}
	
	public Result(int code) {
		this.code = code;
	}
	
	public Result(int code, String msg) {
		this(code);
		this.msg = msg;
	}
	
	public Result(int code, String msg,Object data) {
		this(code,msg);
		this.data = data;
	}
	
	public Result(int code, String msg,Object data,Long total) {
		this(code,msg,data);
		this.total = total;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}
	

}

package com.tongwei.aliyun;

import java.io.Serializable;

/**
 * @author yangz
 * @date 2018年2月28日 下午5:19:10
 * @description 阿里云短信实体类
 */
public class AliyunSms implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/** 电话号码,分隔 */
	private String phones;
	
	/** 签名 */
	private String signName;
	
	/** 模版code */
	private String code;
	
	/** json格式参数 */
	private String params;
	
	/** 短信类型--简单描述 */
	private String type;
	
	public AliyunSms() {
	}

	public AliyunSms(String phones, String signName, String code, String params) {
		this.phones = phones;
		this.signName = signName;
		this.code = code;
		this.params = params;
	}

	public String getPhones() {
		return phones;
	}

	public void setPhones(String phones) {
		this.phones = phones;
	}

	public String getSignName() {
		return signName;
	}

	public void setSignName(String signName) {
		this.signName = signName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}

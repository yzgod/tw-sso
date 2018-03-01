package com.tongwei.aliyun.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author yangz
 * @date 2018年3月1日 上午8:55:29
 * @description 阿里云短信
 */
@Table(name="tp_aliyun_sms")
public class Sms implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	private Integer id;
	
	/** 电话号码,分隔 */
	private String phones;
	
	/** 签名 */
	@Column(name="sign_name")
	private String signName;
	
	/** 模版code */
	private String code;
	
	/** json格式参数 */
	private String params;
	
	/** 短信类型--简单描述 */
	private String type;
	
	@Column(name="send_time")
	private Date sendTime;
	
	public Sms() {
	}

	public Sms(String phones, String signName, String code, String params, String type) {
		this.phones = phones;
		this.signName = signName;
		this.code = code;
		this.params = params;
		this.type = type;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

}

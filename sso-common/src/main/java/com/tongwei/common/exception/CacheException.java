package com.tongwei.common.exception;

import com.tongwei.common.model.ResultCode;

/**
 * @author		yangz
 * @date		2018年1月16日 上午10:52:38
 * @description	缓存异常
 */
public class CacheException extends ApplicationException{

	private static final long serialVersionUID = -2678203134198782909L;
	
	public static final String MESSAGE = "缓存异常";

	public CacheException() {
		super(MESSAGE);
	}

	public CacheException(String message) {
		super(message);
		this.code = ResultCode.CACHE_ERROR;
	}
	
	public CacheException(int code, String message) {
		super(message);
		this.code = code;
	}

	public CacheException(String message, Throwable cause) {
		super(message, cause);
		this.code = ResultCode.CACHE_ERROR;
	}
	
	public CacheException(int code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public CacheException(Throwable cause) {
		super(cause);
		this.code = ResultCode.CACHE_ERROR;
	}
}

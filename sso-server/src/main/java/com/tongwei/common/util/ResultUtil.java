package com.tongwei.common.util;

import com.tongwei.common.model.Result;
import com.tongwei.common.model.ResultCode;

/**
 * <p> 介绍: 返回结果的处理工具类
 * <p> Result
 * @author 		yangz
 * @date 		2017年6月9日 上午10:30:53
 */
public class ResultUtil {

	// success
	public static Result doSuccess() {
		return doSuccess(null, null, null);
	}

	public static Result doSuccess(String msg) {
		return doSuccess(msg, null, null);
	}

	public static  Result doSuccess(Object data) {
		return doSuccess(null, data, null);
	}

	public static  Result doSuccess(String msg, Object data) {
		return doSuccess(msg, data, null);
	}

	public static  Result doSuccess(Object data, Long total) {
		return doSuccess(null, data, total);
	}

	public static  Result doSuccess(String msg, Object data, Long total) {
		return new Result(ResultCode.SUCCESS, msg, data, total);
	}

	// 服务器异常
	public static  Result doFailure() {
		return doFailure(null);
	}

	public static  Result doFailure(String msg) {
		return new Result(ResultCode.ERROR, msg);
	}
	

	// 自定义响应 Status
	public static  Result response(int resultCode) {
		return response(resultCode, null);
	}

	public static  Result response(int resultCode, String msg) {
		return response(resultCode, msg, null);
	}

	public static  Result response(int resultCode, String msg, Object data) {
		return new Result(resultCode, msg, data);
	}
}

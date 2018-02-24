package com.tongwei.sso.config;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tongwei.common.exception.ApplicationException;
import com.tongwei.common.model.Result;
import com.tongwei.common.model.ResultCode;
import com.tongwei.common.util.ResultUtil;

/**
 * @author		yangz
 * @date		2018年1月19日 下午12:04:15
 * @description	全局异常处理
 */
@ControllerAdvice
public class GlobalDefaultExceptionHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalDefaultExceptionHandler.class);

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public Result defaultExceptionHandler(HttpServletRequest req, Exception e) {
		LOGGER.error(e.getMessage(), e);//异常记录
		return ResultUtil.doFailure("服务器异常!");
	}
	
	@ExceptionHandler(ApplicationException.class)
	@ResponseBody
	public Result applicationExceptionHandler(HttpServletRequest req, ApplicationException e) {
		LOGGER.error(e.getMessage(), e);//异常记录
		return ResultUtil.response(ResultCode.APPLICATION_ERROR, e.getMessage());
	}

}
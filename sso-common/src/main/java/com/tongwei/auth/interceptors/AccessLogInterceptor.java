package com.tongwei.auth.interceptors;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.tongwei.auth.log.AccessLog;
import com.tongwei.auth.util.LogUtil;

/**
 * @author		yangz
 * @date		2018年2月5日 上午9:22:57
 * @description	日志记录拦截器
 */
public class AccessLogInterceptor extends HandlerInterceptorAdapter {
	
	private static final ThreadLocal<Long> startTime = new ThreadLocal<Long>();
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		AccessLog logAnno = handlerMethod.getMethodAnnotation(AccessLog.class);
		if(logAnno!=null){
			startTime.set(System.currentTimeMillis());
		}
		return true;
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		AccessLog logAnno = handlerMethod.getMethodAnnotation(AccessLog.class);
		if(logAnno!=null){
	        // start-time
			Long start = startTime.get();
			long cur = System.currentTimeMillis();
			int timeUsed = (int) (cur-start);
			String accessInfo = logAnno.value();
			Map<String, String[]> parameterMap = request.getParameterMap();
			String parameter = null;
			if(!parameterMap.isEmpty()){
				parameter = JSON.toJSONString(request.getParameterMap());
			}
			//log
			LogUtil.logToDbAccess(accessInfo, request.getMethod(), request.getRequestURL().toString(), request.getRemoteHost(), 
					parameter, timeUsed);
			if(ex!=null){
				LogUtil.logUserToFile("access ex :", ex);
			}
		}
	}
}  
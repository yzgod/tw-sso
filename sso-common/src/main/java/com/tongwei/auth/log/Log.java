package com.tongwei.auth.log;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.alibaba.fastjson.JSON;
import com.tongwei.Const;
import com.tongwei.auth.core.SessionCore;

/**
 * @author 		yangz
 * @date 		2018年2月3日 下午6:31:46
 * @description	日志发送到redis,认证中心处理分类
 */
public class Log {
	
	private static final Logger logger = LoggerFactory.getLogger(Log.class);
	
	private StringRedisTemplate redis;
	
	private String appCode;
	
	public Log(StringRedisTemplate redis, String appCode) {
		if(redis==null) {
			throw new IllegalArgumentException("redis不能为空!");
		}
		if(StringUtils.isBlank(appCode)) {
			throw new IllegalArgumentException("appCode不能为空!");
		}
		this.redis = redis;
		this.appCode = appCode;
	}
	
	public void logToDbOp(String msg) {
		String loginName = SessionCore.LoginName.value();
		if(loginName!=null) {
			DBUserLogBean dbLogBean = new DBUserLogBean(appCode,msg,loginName);
			dbLogBean.setType(1);
			addToLogQueue(dbLogBean);
		}else {
			logger.error("Operation log save failure,cause user is null.The log msg is: {}",msg);
		}
	}
	
	public void logToDbAccess(String msg,String method,String url,String ip,String parameter,Integer timeUsed) {
		String loginName = SessionCore.LoginName.value();
		if(loginName!=null) {
			DBAccessUserLogBean dbLogBean = new DBAccessUserLogBean(appCode,msg,loginName,
					method,url,ip,parameter,timeUsed);
			dbLogBean.setType(2);
			addToLogQueue(dbLogBean);
		}else {
			logger.error("Access log save failure,cause user is null.The log msg is: {}",msg);
		}
	}
	
	public void logToFile(String msg) {
		logToFile(msg,null);
	}
	
	public void logToFile(Class<?> clz,String msg) {
		logToFile(clz, msg, null);
	}
	
	public void logToFile(String msg,Throwable error) {
		logToFile(null, msg, error);
	}
	
	private void logToFile(Class<?> clz,String msg,Throwable error) {
		TextLogBean logBean = new TextLogBean(appCode, msg, error, clz, Thread.currentThread().getName());
		logBean.setType(11);
		addToLogQueue(logBean);
	}
	
	public void logUserToFile(String msg) {
		logUserToFile(msg,null);
	}
	
	public void logUserToFile(Class<?> clz,String msg) {
		logUserToFile(clz,msg,null);
	}
	
	public void logUserToFile(String msg,Throwable error) {
		logUserToFile(null,msg,error);
	}
	
	private void logUserToFile(Class<?> clz,String msg,Throwable error) {
		String loginName = SessionCore.LoginName.value();
		if(loginName!=null) {
			TextUserLogBean logBean = new TextUserLogBean(appCode, msg, error, clz, Thread.currentThread().getName(), loginName);
			logBean.setType(12);
			addToLogQueue(logBean);
		}else {
			logger.error("Operation log save failure,cause user is null.The log msg is: {}",msg);
		}
	}
	
	private void addToLogQueue(LogBean bean) {
		redis.opsForList().leftPush(Const.QUEUE_LOG_KEY, JSON.toJSONString(bean));
	}
}

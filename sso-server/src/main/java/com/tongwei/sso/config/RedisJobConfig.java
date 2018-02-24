package com.tongwei.sso.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tongwei.sso.redis.LogHandler;
import com.tongwei.sso.redis.RememberMeHandler;
import com.tongwei.sso.redis.RoleChangeHandler;
import com.tongwei.sso.redis.UserChangeHandler;

/**
 * @author		yangz
 * @date		2018年2月2日 下午12:31:09
 * @description	监听redis队列的任务配置
 */
@Configuration
public class RedisJobConfig {

	@Bean(destroyMethod="destroy")
	public RoleChangeHandler roleChangeHandler(ApplicationContext context){
		RoleChangeHandler roleChangeHandler = new RoleChangeHandler();
		return roleChangeHandler;
	}
	
	@Bean(destroyMethod="destroy")
	public UserChangeHandler userChangeHandler(ApplicationContext context){
		UserChangeHandler userChangeHandler = new UserChangeHandler();
		return userChangeHandler;
	}
	
	@Bean(destroyMethod="destroy")
	public LogHandler logHandler(ApplicationContext context){
		LogHandler logHandler = new LogHandler();
		return logHandler;
	}
	
	@Bean(destroyMethod="destroy")
	public RememberMeHandler rememberMeHandler(ApplicationContext context){
		RememberMeHandler rememberMeHandler = new RememberMeHandler();
		return rememberMeHandler;
	}
	
	
}

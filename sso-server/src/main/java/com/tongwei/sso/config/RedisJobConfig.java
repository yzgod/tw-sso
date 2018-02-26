package com.tongwei.sso.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tongwei.sso.redis.LogHandler;
import com.tongwei.sso.redis.RememberMeHandler;
import com.tongwei.sso.redis.RoleChangeHandler;
import com.tongwei.sso.redis.UserChangeHandler;

/**
 * @author      yangz
 * @date        2018年2月2日 下午12:31:09
 * @description 监听redis队列的任务配置
 */
@Configuration
public class RedisJobConfig {

    @Bean(destroyMethod = "destroy")
    public RoleChangeHandler roleChangeHandler(ApplicationContext context) {
        return new RoleChangeHandler();
    }

    @Bean(destroyMethod = "destroy")
    public UserChangeHandler userChangeHandler(ApplicationContext context) {
        return new UserChangeHandler();
    }

    @Bean(destroyMethod = "destroy")
    public LogHandler logHandler(ApplicationContext context) {
        return new LogHandler();
    }

    @Bean(destroyMethod = "destroy")
    public RememberMeHandler rememberMeHandler(ApplicationContext context) {
        return new RememberMeHandler();
    }

}

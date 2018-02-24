package com.tongwei.auth.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;

import com.tongwei.auth.config.SsoConfiguration;
import com.tongwei.auth.config.SsoRedisAutoConfiguration;
import com.tongwei.auth.security.RememberMeType;
import com.tongwei.auth.security.rule.CookieRememberMeRule;
import com.tongwei.auth.security.rule.RememberMeRule;

/**
 * @author		yangz
 * @date		2018年1月16日 下午3:50:30
 * @description	启用权限认证
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.TYPE })
@Documented
@Import({SsoConfiguration.class,SsoRedisAutoConfiguration.class})
@EnableRedisHttpSession
@Configuration
public @interface EnableSSO {
	
	/** ssofilter顺序 */
	int filterOrder() default -100;

	/** 是否启动菜单url的匹配,默认关闭 */
	boolean enableMenuPattern() default false;
	
	/** 是否启用记住功能 */
	boolean enableRememberMe() default false;
	
	/** 记住验证规则 */
	RememberMeType rememberMeType() default RememberMeType.USER_AGENT;
	
	/** 启用注解验证权限 */
	boolean enableAuthAnnotation() default false;
	
	/** 权限拦截器拦截地址 */
	String[] authAnnotationUrlPatterns() default "/**";
	
	/** 权限拦截器不拦截得地址 */
	String[] authAnnotationUrlExcludePathPatterns() default "/static";
	
	/** AccessLogInterceptor启用与否 */
	boolean enableAccessLog() default false;
	
	/** 访问日志拦截器拦截地址 */
	String[] accessLogUrlPatterns() default "/**";
	
	/** 访问日志拦截器不拦截得地址 */
	String[] accessLogUrlExcludePathPatterns() default "/static";
	
	Class<? extends RememberMeRule> rememberMeRule() default CookieRememberMeRule.class;
	
	/** 
	 * sessionid的策略,默认cookie实现
	 * header方式见org.springframework.session.web.http.HeaderHttpSessionStrategy
	 * 自定义策略实现org.springframework.session.web.http.MultiHttpSessionStrategy
	 **/
    Class<? extends HttpSessionStrategy> httpSessionStrategy() default CookieHttpSessionStrategy.class;
    
	/**session过期时间*/
	int maxInactiveIntervalInSeconds() default 1800;

}

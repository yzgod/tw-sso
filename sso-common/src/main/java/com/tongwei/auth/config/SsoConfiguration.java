package com.tongwei.auth.config;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.session.web.http.HttpSessionStrategy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.tongwei.Const;
import com.tongwei.auth.anno.EnableSSO;
import com.tongwei.auth.filter.SsoFilter;
import com.tongwei.auth.interceptors.AccessLogInterceptor;
import com.tongwei.auth.interceptors.AuthorizationAttributeSourceInterceptor;
import com.tongwei.auth.log.Log;
import com.tongwei.auth.security.Auth;
import com.tongwei.auth.security.RememberMeType;
import com.tongwei.auth.security.rule.AllUsersAcess;
import com.tongwei.auth.security.rule.LoginAccessRule;
import com.tongwei.auth.security.rule.RememberMeRule;
import com.tongwei.auth.servlet.SetCookieServlet;
import com.tongwei.auth.util.AuthUtil;
import com.tongwei.auth.util.LogUtil;
import com.tongwei.auth.util.SessionUtil;
import com.tongwei.auth.util.SyncUtil;

/**
 * @author		yangz
 * @date		2018年1月16日 下午3:49:00
 * @description	配置类
 */
@Configuration
@EnableConfigurationProperties(SsoProperties.class)
public class SsoConfiguration extends WebMvcConfigurerAdapter implements ImportAware{
	
//	private static final Logger logger = LoggerFactory.getLogger(SsoConfiguration.class);
	
	@Bean
    public FilterRegistrationBean filterRegistration(SsoProperties properties,SsoFilter ssoFilter,HttpSessionStrategy httpSessionStrategy) {
		FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(ssoFilter);
        registration.addUrlPatterns("/*");
        registration.addInitParameter("anno", properties.getAnno());
        registration.addInitParameter("loginUrl", properties.getLoginUrl());
        registration.addInitParameter("staticSuffixs", properties.getStaticSuffixs());
        registration.addInitParameter("enableMenuPattern", String.valueOf(enableMenuPattern || properties.isEnableMenuPattern()));
        registration.addInitParameter("successUrl", properties.getSuccessUrl());
        registration.addInitParameter("enableRememberMe", String.valueOf(enableRememberMe || properties.isEnableRememberMe()));
        registration.addInitParameter("rememberMeType", rememberMeType.toString());
        registration.addInitParameter("rememberMeExpireTime", String.valueOf(properties.getRememberMeExpireTime()));
        registration.setName("ssoFilter");
        registration.setOrder(filterOrder);
        return registration;
    }
	
	@Bean
	public ServletRegistrationBean servletRegistrationBean(SsoProperties properties){
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new SetCookieServlet());
		if(properties.isEnableRememberMe()){
			servletRegistrationBean.addInitParameter("rememberMeExpireTime", String.valueOf(properties.getRememberMeExpireTime()));
		}
		servletRegistrationBean.addUrlMappings(Const.SET_COOKIE_SERVLET_PATH);
		return servletRegistrationBean;
	}
	
	@Bean
	public SsoFilter ssoFilter(SsoProperties properties,Auth auth,RememberMeRule rememberMeRule){
		if(!properties.isCenter()){
			auth.noneCenterOperation();
		}
		SsoFilter ssoFilter = new SsoFilter();
		return ssoFilter;
	}
	
	@Bean
	public Auth auth(SsoProperties properties) {
		Auth auth = new Auth();
		if(StringUtils.isBlank(properties.getAppCode())){
			throw new IllegalArgumentException("请在application.properties中配置sso.sys.appCode属性!");
		}
		auth.setAppCode(properties.getAppCode());
		return auth;
	}
	
	@Bean
	public AuthUtil authUtil(){
		AuthUtil authUtil = new AuthUtil();
		return authUtil;
	}
	
	@Bean
	public SyncUtil syncUtil(){
		SyncUtil syncUtil = new SyncUtil();
		return syncUtil;
	}
	
	@Bean
    public HttpSessionStrategy httpSessionStrategy() throws InstantiationException, IllegalAccessException {
		return httpSessionStrategy.newInstance();
    }
	
	@Bean
	public RememberMeRule rememberMeRule() throws InstantiationException, IllegalAccessException {
		return rememberMeRule.newInstance();
	}
	
	@Bean
	public Log log(StringRedisTemplate redis,SsoProperties properties) {
		return new Log(redis,properties.getAppCode());
	}
	
	@Bean
	public LogUtil logUtil() {
		return new LogUtil();
	}
	
	@Bean
	public SessionUtil sessionUtil() {
		return new SessionUtil();
	}
	
	@Bean
	public LoginAccessRule loginAccessRule() {
		return new AllUsersAcess();
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		if(enableAuthAnnotation){
			registry.addInterceptor(new AuthorizationAttributeSourceInterceptor())
			.addPathPatterns(authAnnotationUrlPatterns)
			.excludePathPatterns(authAnnotationUrlExcludePathPatterns);
		}
		if(enableAccessLog){
			registry.addInterceptor(new AccessLogInterceptor())
				.addPathPatterns(accessLogUrlPatterns)
				.excludePathPatterns(accessLogUrlExcludePathPatterns);
		}
	}
	
	@Override
	public void setImportMetadata(AnnotationMetadata importMetadata) {
		Map<String, Object> enableAttrMap = importMetadata.getAnnotationAttributes(EnableSSO.class.getName());
		AnnotationAttributes enableAttrs = AnnotationAttributes.fromMap(enableAttrMap);
		this.enableMenuPattern = enableAttrs.getBoolean("enableMenuPattern");
		this.enableRememberMe = enableAttrs.getBoolean("enableRememberMe");
		rememberMeType = enableAttrs.getEnum("rememberMeType");
		filterOrder = enableAttrs.getNumber("filterOrder");
		enableAccessLog = enableAttrs.getBoolean("enableAccessLog");
		accessLogUrlPatterns = enableAttrs.getStringArray("accessLogUrlPatterns");
		accessLogUrlExcludePathPatterns = enableAttrs.getStringArray("accessLogUrlExcludePathPatterns");
		enableAuthAnnotation = enableAttrs.getBoolean("enableAuthAnnotation");
		authAnnotationUrlPatterns = enableAttrs.getStringArray("authAnnotationUrlPatterns");
		authAnnotationUrlExcludePathPatterns = enableAttrs.getStringArray("authAnnotationUrlExcludePathPatterns");
		httpSessionStrategy = enableAttrs.getClass("httpSessionStrategy");
		rememberMeRule = enableAttrs.getClass("rememberMeRule");
	}
	
	private boolean enableMenuPattern;
	
	private boolean enableRememberMe;
	
	private RememberMeType rememberMeType;
	
	private int filterOrder;
	
	private boolean enableAccessLog;
	
	private String[] accessLogUrlPatterns;
	
	private String[] accessLogUrlExcludePathPatterns;
	
	private boolean enableAuthAnnotation;

	private String[] authAnnotationUrlPatterns;

	private String[] authAnnotationUrlExcludePathPatterns;
	
	private Class<? extends HttpSessionStrategy> httpSessionStrategy;
	
	private Class<? extends RememberMeRule> rememberMeRule;
	

}

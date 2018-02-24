package com.tongwei.auth.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import com.tongwei.Const;
import com.tongwei.auth.security.Auth;
import com.tongwei.auth.security.RememberMeType;

/**
 * @author yangz
 * @date 2018年1月16日 下午4:23:19
 * @description
 */
public abstract class AbstractAuthFilter implements Filter {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());

	// anno匿名访问地址
	protected List<String> annoList = new ArrayList<>();
	// 排除静态资源endWith
	protected List<String> staticSuffixList = null;

	protected PathMatcher pathMatcher = new AntPathMatcher();
	// 是否启用菜单pattern匹配
	protected boolean enableMenuPattern;
	
	// 认证服务地址
	protected String loginUrl;
	
	// 登录成功后跳转的应用地址
	protected String successUrl;
	
	// 应用编码
	protected String appCode;
	
	// 权限
	protected Auth auth;
	
	// 记住登录的过期时间秒数,默认7天
	protected int rememberMeExpireTime = 7*24*3600;
	// 是否启用记住登录功能
	protected boolean enableRememberMe;
	// 记住规则
	protected RememberMeType rememberMeType = RememberMeType.USER_AGENT;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
		loginUrl = filterConfig.getInitParameter("loginUrl");
		if (StringUtils.isBlank(loginUrl)) {
			throw new IllegalArgumentException("sso.sys.loginUrl不能为空!");
		}
		logger.info("loginUrl is "+loginUrl);
		
		String enableRememberMeStr = filterConfig.getInitParameter("enableRememberMe");
		if(StringUtils.isNotBlank(enableRememberMeStr)){
			enableRememberMe = Boolean.valueOf(enableRememberMeStr.trim());
			if(enableRememberMe){
				//记住登录过期时间
				String expireTimeStr = filterConfig.getInitParameter("rememberMeExpireTime");
				if(StringUtils.isNotBlank(expireTimeStr)){
					rememberMeExpireTime = Integer.valueOf(expireTimeStr.trim());
				}
				logger.info("记住登录启用成功,记住登录后的过期时间为: "+rememberMeExpireTime/3600/24 +"天");
				String rememberMeTypeStr = filterConfig.getInitParameter("rememberMeType");
				if("HOST".equalsIgnoreCase(rememberMeTypeStr)){
					rememberMeType = RememberMeType.HOST;
					logger.info("记住登录规则:HOST");
				}else if("USER_AGENT".equalsIgnoreCase(rememberMeTypeStr)){
					rememberMeType = RememberMeType.USER_AGENT;
					logger.info("记住登录规则:USER_AGENT");
				}else{
					rememberMeType = RememberMeType.NONE;
					logger.info("记住登录规则:NONE");
				}
			}
		}
		
		//添加默认排除的页面
		annoList.add("/login.jsp");
		annoList.add("/login");
		annoList.add(Const.SET_COOKIE_SERVLET_PATH);
		
		String anno = filterConfig.getInitParameter("anno");
		if (StringUtils.isNotBlank(anno)) {
			List<String> asList = Arrays.asList(anno.split(","));
			annoList.addAll(asList);
		}
		logger.info("anno url list:"+annoList);
		
		//静态资源排除
		String staticSuffixs = filterConfig.getInitParameter("staticSuffixs");
		if (StringUtils.isNotBlank(staticSuffixs)) {
			staticSuffixList = Arrays.asList(staticSuffixs.split(","));
			logger.info("staticSuffixList:"+staticSuffixList);
		}
		
		//登录成功后跳转页面
		successUrl = filterConfig.getInitParameter("successUrl");
		logger.info("The login successful url is "+successUrl);
		
		//是否进行菜单url匹配
		String enableMenuPatternStr = filterConfig.getInitParameter("enableMenuPattern").trim();
		if (StringUtils.isNotBlank(enableMenuPatternStr) && enableMenuPatternStr.equalsIgnoreCase("true") ) {
			enableMenuPattern = true;
			logger.info("MenuPattern is enabled!");
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		
		String path = httpRequest.getServletPath();
		//验证是否是否符合排除的地址
		if (staticSuffixsPath(path) || matchAnnoPath(path)){
			chain.doFilter(request, response);
		} else {
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			doFilter(httpRequest, httpResponse, chain);
		}
	}

	private boolean matchAnnoPath(String path) {
		for (String ignore : annoList) {
			if (pathMatcher.match(ignore, path)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean staticSuffixsPath(String path) {
		if (staticSuffixList != null) {
			for (String ignore : staticSuffixList) {
				if(path.endsWith(ignore)){
					return true;
				}
			}
		}
		return false;
	}

	public abstract void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException;

	@Override
	public void destroy() {
	}

}

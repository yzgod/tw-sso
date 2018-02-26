package com.tongwei.auth.filter;

import java.io.IOException;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.session.web.http.HttpSessionStrategy;

import com.tongwei.auth.core.SessionCore;
import com.tongwei.auth.model.Menu;
import com.tongwei.auth.security.rule.LoginAccessRule;
import com.tongwei.auth.security.rule.RememberMeRule;
import com.tongwei.auth.util.AuthUtil;
import com.tongwei.common.util.ResponseUtil;

/**
 * @author yangz
 * @date 2018年1月16日 下午3:32:54
 * @description 单点登录/权限 验证 核心filter
 */
public class SsoFilter extends AbstractAuthFilter implements BeanFactoryAware {

    private RememberMeRule rememberMeRule;

    private LoginAccessRule loginAccessRule;
    // session策略
    private HttpSessionStrategy httpSessionStrategy;

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        Integer userId = SessionCore.UserId.value();
        if (userId == null) { // session验证失败
            if (enableRememberMe) {// 验证记住功能
                boolean success = rememberMeRule.validate(request, response, chain, rememberMeType,
                        rememberMeExpireTime);
                if (!success) {
                    response.sendRedirect(loginUrl + "?successUrl=" + successUrl + "&rememberMeType=" + rememberMeType);
                }
            } else {
                response.sendRedirect(loginUrl + "?successUrl=" + successUrl);
            }
            return;
        }

        // 验证用户是否符合访问该子系统的规则
        if (!loginAccessRule.isAccess()) {
            ResponseUtil.responseUnAuthorizedJson(response);
            return;
        }
        // url匹配逻辑
        if (enableMenuPattern && !menuPatternPatch(request)) {
            ResponseUtil.responseUnAuthorizedJson(response);
            return;
        }
        chain.doFilter(request, response);
    }

    /**
     * 匹配逻辑
     */
    private boolean menuPatternPatch(HttpServletRequest req) {
        String servletPath = req.getServletPath();
        Set<Menu> menus = AuthUtil.getMenus();
        for (Menu menu : menus) {
            String pattern = menu.getPattern();
            if (StringUtils.isNotBlank(pattern)) {
                return pathMatcher.match(pattern, servletPath);
            }
        }
        return false;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        httpSessionStrategy = beanFactory.getBean(HttpSessionStrategy.class);
        logger.info("httpSessionStrategy is " + httpSessionStrategy.getClass().getName());
        rememberMeRule = beanFactory.getBean(RememberMeRule.class);
        logger.info("rememberMeRule is " + rememberMeRule.getClass().getName());
        loginAccessRule = beanFactory.getBean(LoginAccessRule.class);
        logger.info("LoginAccessRule is " + rememberMeRule.getClass().getName());

        Validate.notNull(httpSessionStrategy, "httpSessionStrategy is null");
        Validate.notNull(rememberMeRule, "rememberMeRule is null");
        Validate.notNull(loginAccessRule, "loginAccessRule is null");
    }

}

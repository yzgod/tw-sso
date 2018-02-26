package com.tongwei.auth.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

/**
 * @author yangz
 * @date 2018年1月25日 下午2:04:12
 * @description 用于认证中心跳转后跨域名的cookie设置
 */
public class SetCookieServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private int rememberMeExpireTime = 7 * 24 * 3600;// 单位秒

    @Override
    public void init(ServletConfig config) throws ServletException {
        String rememberMeExpireTimeStr = config.getInitParameter("rememberMeExpireTime");
        if (StringUtils.isNotBlank(rememberMeExpireTimeStr)) {
            rememberMeExpireTime = Integer.valueOf(rememberMeExpireTimeStr);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String SESSION = req.getParameter("SESSION");
        String AUTHUSER = req.getParameter("AUTHUSER");
        String toUrl = req.getParameter("toUrl");

        if (StringUtils.isNotBlank(SESSION)) {
            Cookie cookie = new Cookie("SESSION", SESSION);
            cookie.setPath("/");
            resp.addCookie(cookie);
        }
        if (StringUtils.isNotBlank(AUTHUSER)) {
            Cookie cookie = new Cookie("AUTHUSER", AUTHUSER);
            cookie.setPath("/");
            cookie.setMaxAge(rememberMeExpireTime);
            resp.addCookie(cookie);
        }
        if (StringUtils.isBlank(toUrl)) {// toUrl为空,默认跳转/
            toUrl = "/";
        }
        resp.sendRedirect(toUrl);
    }

}

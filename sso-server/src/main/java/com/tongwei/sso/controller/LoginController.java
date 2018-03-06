package com.tongwei.sso.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tongwei.auth.model.User;
import com.tongwei.auth.security.RememberMeType;
import com.tongwei.auth.security.rule.RememberMeRule;
import com.tongwei.common.BaseController;
import com.tongwei.common.exception.AuthenticationExcption;
import com.tongwei.common.model.Result;
import com.tongwei.common.util.ResultUtil;
import com.tongwei.sso.util.AuthService;

/**
 * @author yangz
 * @date 2018年2月26日 上午10:29:53
 * @description 登录注销
 */
@Controller
@ConfigurationProperties(prefix = "sso.sys")
public class LoginController extends BaseController {

    private String successUrl;
    
    private String setCookieUrl;

    @Autowired
    AuthService authService;

    @Autowired
    RememberMeRule rememberMeRule;

    @PostMapping("/login")
    @ResponseBody
    public Result login(String loginName, String password, String successUrl, String rememberMe, String rememberMeType)
            throws Exception {
        if (StringUtils.isBlank(loginName) || StringUtils.isBlank(password)) {
            return ResultUtil.doFailure("用户名或密码不能为空!");
        }
        User user = null;
        try {
            user = authService.login(loginName, password);
        } catch (AuthenticationExcption e) {
            return ResultUtil.doFailure(e.getMessage());
        }

        if ("null".equals(successUrl) || StringUtils.isBlank(successUrl)) {
            successUrl = this.successUrl;
        }

        Map<String, String> data = new HashMap<>(2);

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                String value = cookie.getValue();
                if ("AUTHUSER".equalsIgnoreCase(name)) {
                    data.put("AUTHUSER", value);
                }
            }
        }

        // 记住我
        if ("on".equals(rememberMe)) {
            RememberMeType re = RememberMeType.USER_AGENT;
            if ("HOST".equalsIgnoreCase(rememberMeType)) {
                re = RememberMeType.HOST;
            }
            if ("NONE".equalsIgnoreCase(rememberMeType)) {
                re = RememberMeType.NONE;
            }
            String generateValue = rememberMeRule.generateValue(request, re, user.getId());
            if (generateValue != null) {
                data.put("AUTHUSER", generateValue);
            }
        }
        data.put("setCookieUrl", setCookieUrl);
        data.put("successUrl", successUrl);
        data.put("SESSION", request.getSession().getId());
        return ResultUtil.doSuccess(data);
    }

    // 注销登录
    @GetMapping(value = "/loginout")
    public String loginout() {// COOKIE策略注销
        authService.loginout();
        Cookie cookie = new Cookie("AUTHUSER", "");
        response.addCookie(cookie);
        return "redirect:/";
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public String getSetCookieUrl() {
        return setCookieUrl;
    }

    public void setSetCookieUrl(String setCookieUrl) {
        this.setCookieUrl = setCookieUrl;
    }

}
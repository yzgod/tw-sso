package com.tongwei.sso.tag;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author yangz
 * @date 2018年2月26日 上午10:41:04
 * @description context path
 */
public class CtxTag extends TagSupport {

    private static final long serialVersionUID = 1977225350330714103L;

    @Override
    public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        String contextPath = request.getContextPath();
        try {
            out.write(contextPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.doStartTag();
    }

}

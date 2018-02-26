package com.tongwei.auth.taglibs.jsp;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.tongwei.auth.util.AuthUtil;

/**
 * @author yangz
 * @date 2018年2月7日 上午10:02:53
 * @description 所有角色
 */
public class HasRolesAllTagSupport extends TagSupport {
    private static final long serialVersionUID = 1L;

    private String value;

    @Override
    public int doStartTag() throws JspException {
        String[] roleCodes = value.split(",");
        boolean hasRolesAll = AuthUtil.hasRolesAll(roleCodes);
        if (hasRolesAll) {
            return EVAL_BODY_INCLUDE;
        }
        return SKIP_BODY;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}

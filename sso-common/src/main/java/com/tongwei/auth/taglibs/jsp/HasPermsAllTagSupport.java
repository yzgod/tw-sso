package com.tongwei.auth.taglibs.jsp;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.tongwei.auth.helper.AuthValidateHelper;

/**
 * @author		yangz
 * @date		2018年2月7日 上午10:02:53
 * @description	
 */
public class HasPermsAllTagSupport extends TagSupport {
	private static final long serialVersionUID = 1L;
	
	private String value;
	
	@Override
	public int doStartTag() throws JspException {
		String[] permGroups = value.split("\\|");
		boolean hasPermAll = AuthValidateHelper.hasPermAll(permGroups);
		if(hasPermAll){
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

package com.tongwei.auth.core;

import com.tongwei.auth.util.SessionUtil;

/**
 * @author		yangz
 * @date		2018年2月23日 下午3:42:21
 * @description	session中存储的用户变量,请求上下文中使用
 */
public enum SessionCore {
	
	UserId {
		@SuppressWarnings("unchecked")
		@Override
		public Integer value() {
			return SessionUtil.getUserId();
		}
	},
	LoginName{
		@SuppressWarnings("unchecked")
		@Override
		public String value() {
			return SessionUtil.getLoginName();
		}
	},
	CellPhone{
		@SuppressWarnings("unchecked")
		@Override
		public String value() {
			return SessionUtil.getCellPhone();
		}
	},
	RealName{
		@SuppressWarnings("unchecked")
		@Override
		public String value() {
			return SessionUtil.getRealName();
		}
	};
	
	public <T> T value() {
		return null;
	}
	
}

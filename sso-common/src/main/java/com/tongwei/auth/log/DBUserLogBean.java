package com.tongwei.auth.log;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author yangz
 * @date 2018年2月3日 下午8:02:24
 * @description 持久化到mysql的日志,比如操作日志
 */
@Table(name = "tp_log_operate")
public class DBUserLogBean extends LogBean {
    private static final long serialVersionUID = 1L;

    public DBUserLogBean() {
    }

    public DBUserLogBean(String appCode, String msg, String loginName) {
        super(appCode, msg);
        this.loginName = loginName;
    }

    // 操作人登陆名
    @Column(name = "login_name")
    protected String loginName;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

}

package com.tongwei.sso.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author yangz
 * @date 2018年2月8日 下午4:28:09
 * @description 登录日志
 */
@Table(name = "tp_log_login")
public class LoginLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Integer id;

    @Column(name = "login_name")
    private String loginName;

    @Column(name = "real_name")
    private String realName;

    private String ip;

    @Column(name = "create_date")
    private Date createDate = new Date();

    // 0-用户注销,1-用户登录,2-不允许登录,3-管理员强制下线
    private Integer type;

    public LoginLog() {
    }

    public LoginLog(String loginName, String realName, String ip, Integer type) {
        this.loginName = loginName;
        this.realName = realName;
        this.ip = ip;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

}

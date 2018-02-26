package com.tongwei.auth.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author yangz
 * @date 2018年1月16日 下午2:47:20
 * @description 用户信息类
 */
@Table(name = "ts_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /*** 用户id */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /*** 登录名 */
    @Column(name = "login_name")
    private String loginName;

    /*** 真实名字 */
    @Column(name = "real_name")
    private String realName;

    /*** 手机号码 */
    @Column(name = "cell_phone")
    private String cellPhone;

    /*** 邮箱 */
    private String email;

    /*** 密码 */
    @JSONField(serialize = false)
    private String password;

    /*** 创建时间 */
    @Column(name = "create_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /*** 禁用与否 */
    private Boolean forbidden;

    /*** 备注 */
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getForbidden() {
        return forbidden;
    }

    public void setForbidden(Boolean forbidden) {
        this.forbidden = forbidden;
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

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}

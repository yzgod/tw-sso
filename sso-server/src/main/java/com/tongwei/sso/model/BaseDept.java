package com.tongwei.sso.model;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author      yangz
 * @date        2018年2月26日 下午3:19:14
 * @description 基础部门
 */
@Table(name="ts_base_dept")
public class BaseDept implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    private Integer id;
    
    private String name;
    
    private String code;
    
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    
}

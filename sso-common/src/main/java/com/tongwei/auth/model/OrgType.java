package com.tongwei.auth.model;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author yangz
 * @date 2018年1月29日 上午10:49:52
 * @description 组织类型
 */
@Table(name = "ts_org_type")
public class OrgType implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Integer id;

    /** 类型名称 */
    private String name;

    /** 类型编码 */
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

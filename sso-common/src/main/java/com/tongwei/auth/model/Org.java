package com.tongwei.auth.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author yangz
 * @date 2018年1月16日 下午2:54:05
 * @description 组织机构类 集团公司/总公司/区域/子公司/事业部/普通部门等
 */
@Table(name = "ts_org")
public class Org implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Integer id;

    /** 组织名称 */
    private String name;

    /** 组织编码 */
    private String code;

    /** 组织类型id */
    @Column(name = "type_id")
    private Integer typeId;

    /** 父组织id */
    @Column(name = "parent_id")
    private Integer parentId;

    /** 组织排序 */
    private Integer ord;

    /** 备注 */
    private String remark;

    private String f1;

    private String f2;

    @Transient
    private String typeName;

    /** 是否为主组织 */
    @Transient
    private Boolean isDefault;

    /** 父公司 */
    @Transient
    private Org parentOrg;

    /** 公司类型 */
    @Transient
    private OrgType orgType;

    @Transient
    private List<?> children;

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

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getOrd() {
        return ord;
    }

    public void setOrd(Integer ord) {
        this.ord = ord;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Org getParentOrg() {
        return parentOrg;
    }

    public void setParentOrg(Org parentOrg) {
        this.parentOrg = parentOrg;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getF1() {
        return f1;
    }

    public void setF1(String f1) {
        this.f1 = f1;
    }

    public String getF2() {
        return f2;
    }

    public void setF2(String f2) {
        this.f2 = f2;
    }

    public OrgType getOrgType() {
        return orgType;
    }

    public void setOrgType(OrgType orgType) {
        this.orgType = orgType;
    }

    public List<?> getChildren() {
        return children;
    }

    public void setChildren(List<?> children) {
        this.children = children;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

}

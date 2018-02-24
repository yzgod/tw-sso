package com.tongwei.auth.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author		yangz
 * @date		2018年1月16日 下午2:54:05
 * @description	用户组
 */
@Table(name="ts_user_group")
public class UserGroup implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	private Integer id;
	
	/** 组名称 */
	private String name;
	
	/** 用户组编码 */
	private String code;
	
	/** 父组id */
	@Column(name="parent_id")
	private Integer parentId;
	
	/** 组排序 */
	private Integer ord;
	
	/** 备注 */
	private String remark;
	
	private String f1;
	
	private String f2;
	
	/** 父组 */
	@Transient
	private UserGroup parentGroup;
	
	@Transient
	private List<UserGroup> children;

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

	public Integer getOrd() {
		return ord;
	}

	public void setOrd(Integer ord) {
		this.ord = ord;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public UserGroup getParentGroup() {
		return parentGroup;
	}

	public void setParentGroup(UserGroup parentGroup) {
		this.parentGroup = parentGroup;
	}

	public List<UserGroup> getChildren() {
		return children;
	}

	public void setChildren(List<UserGroup> children) {
		this.children = children;
	}
	
}

package com.tongwei.auth.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author		yangz
 * @date		2018年1月16日 下午2:54:58
 * @description	岗位类
 */
@Table(name="ts_position")
public class Position implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	private Integer id;
	
	/** 岗位名 */
	private String name;
	
	/** 岗位编码 */
	private String code;
	
	/** 岗位排序 */
	private Integer ord;
	
	/** 父岗位 */
	@Column(name="parent_id")
	private Integer parentId;
	
	/** 
	 * 关联组织id 
	 */
	@Column(name="org_id")
	private Integer orgId;
	
	/** 备注 */
	private String remark;
	
	/** 是否为主岗位,不持久化,其他表字段,多表可以查询到 */
	@Transient
	private Boolean isDefault;
	
	/** 父岗位 */
	@Transient
	private Position parentPosition;
	
	/** 子岗位 */
	@Transient
	private List<Position> children;

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

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}
	
	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Position getParentPosition() {
		return parentPosition;
	}

	public void setParentPosition(Position parentPosition) {
		this.parentPosition = parentPosition;
	}
	
	public List<Position> getChildren() {
		return children;
	}

	public void setChildren(List<Position> children) {
		this.children = children;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}

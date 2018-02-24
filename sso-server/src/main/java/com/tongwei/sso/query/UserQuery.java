package com.tongwei.sso.query;

import com.tongwei.auth.model.User;
import com.tongwei.common.dao.BaseQuery;

/**
 * @author		yangz
 * @date		2018年1月29日 下午12:43:19
 * @description	用户分页高级查询
 */
public class UserQuery extends BaseQuery<User>{
	private static final long serialVersionUID = 1L;
	
	//组织id
	private Integer oId;
	
	//用户组id
	private Integer ugId;
	
	//岗位id
	private Integer posId;
	
	//关键字查询
	private String keyword;
	

	public Integer getoId() {
		return oId;
	}

	public void setoId(Integer oId) {
		this.oId = oId;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Integer getUgId() {
		return ugId;
	}

	public void setUgId(Integer ugId) {
		this.ugId = ugId;
	}

	public Integer getPosId() {
		return posId;
	}

	public void setPosId(Integer posId) {
		this.posId = posId;
	}
}

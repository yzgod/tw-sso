package com.tongwei.auth.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author		yangz
 * @date		2018年1月17日 下午3:58:28
 * @description	根据用户id查询的redis用户对象
 */
public class AuthUser implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/*** 用户基本信息(不包含密码字段) */
	private User user;
	
	/*** 所属直接组织集合/直接组织中包含父组织,递归 */
	private List<Org> orgs;
	
	/*** 所属直接用户组集合,包含父组,递归 */
	private List<UserGroup> groups;
	
	/*** 拥有的岗位集合,父岗位递归 */
	private List<Position> positions;
	
	/*** 
	 * 拥有的角色(包含父角色)集合set
	 * 计算获得, 包括用户/直接组织/父组织 直接组/父组  直接岗/父岗位 
	 **/
	private Set<Role> roles;
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Org> getOrgs() {
		return orgs;
	}

	public void setOrgs(List<Org> orgs) {
		this.orgs = orgs;
	}

	public List<Position> getPositions() {
		return positions;
	}

	public void setPositions(List<Position> positions) {
		this.positions = positions;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public List<UserGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<UserGroup> groups) {
		this.groups = groups;
	}
	
}

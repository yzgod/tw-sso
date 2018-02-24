package com.tongwei.sso.mapper;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.tongwei.auth.model.Role;
import com.tongwei.common.dao.CmMapper;

/**
 * @author		yangz
 * @date		2018年1月17日 下午2:03:33
 * @description	角色
 */
public interface RoleMapper extends CmMapper<Role> {
	
	/** 根据组织id集合查询公司角色,角色中包含递归的父角色 */
	List<Role> queryRolesByOrgIds(@Param("ids") Set<Integer> ids);
	
	/** 根据用户组id集合查询用户组角色,角色中包含递归的父角色 */
	List<Role> queryRolesByUserGroupIds(@Param("ids") Set<Integer> ids);
	
	/** 根据岗位id集合查询岗位角色,角色中包含递归的父角色 */
	List<Role> queryRolesByPositionIds(@Param("ids") Set<Integer> ids);
	
	/** 根据用户id查询角色,角色中包含递归的父角色 */
	List<Role> queryRolesByUserId(Integer userId);
	
	/** 根据组织id查询角色,角色中不包含递归的父角色 */
	List<Role> queryRolesByOrgId(Integer oId);
	
	//根据菜单的id查询拥有该菜单的 直接角色 列表
	List<Role> getRolesByMenuId(Integer mId);
	
	//保存角色和菜单的级联关系
	void saveRoleMenu(@Param("roleId") Integer roleId,@Param("menuId") Integer menuId);

	//删除角色和菜单的级联关系
	void deleteRoleMenu(@Param("menuId") Integer menuId,@Param("roleIds") Set<Integer> roleIds);
	
	//根据权限的id查询拥有该权限的 直接角色 列表
	List<Role> getRolesByPermId(Integer permId);
	
	//保存角色和权限的级联关系
	void saveRolePerm(@Param("roleId") Integer roleId,@Param("permId") Integer permId);
	
	//删除角色和权限的级联关系
	void deleteRolePerm(@Param("permId") Integer menuId,@Param("roleIds") Set<Integer> roleIds);
	
	//check角色是否拥有权限
	boolean checkHasPerm(Integer roleId);
	
	//check角色是否拥有菜单
	boolean checkHasMenu(Integer roleId);
	
	//check角色是否分配给了用户
	boolean checkBelongUser(Integer roleId);
	
	//check角色是否分配给了用户组
	boolean checkBelongUserGroup(Integer roleId);
	
	//check角色是否分配给了组织
	boolean checkBelongOrg(Integer roleId);
	
	//check角色是否分配给了岗位
	boolean checkBelongPosition(Integer roleId);
	
	//删除
	void deleteRoleMenuByRoleId(Integer roleId);
	
	//删除
	void deleteRolePermByRoleId(Integer roleId);
	
	//根据用户id获取角色id集合
	List<Integer> getRoleIdsByUserId(Integer userId);
	
	//删除
	void deleteUserRoleByUserId(Integer userId);
	
	//保存用户与角色级联关系
	void saveUserRole(@Param("userId") Integer userId,@Param("roleId") Integer roleId);
	
	//根据组织id获取角色id集合
	List<Integer> getRoleIdsByOrgId(Integer orgId);
	
	//删除
	void deleteOrgRoleByOrgId(Integer orgId);
	
	//保存组织与角色级联关系
	void saveOrgRole(@Param("orgId") Integer orgId,@Param("roleId") Integer roleId);
	
	//根据岗位id获取角色id集合
	List<Integer> getRoleIdsByPositionId(Integer positionId);
	
	//删除
	void deletePositionRoleByPositionId(Integer positionId);
	
	//保存岗位与角色级联关系
	void savePositionRole(@Param("positionId") Integer positionId,@Param("roleId") Integer roleId);
	
	//根据用户组id获取角色id集合
	List<Integer> getRoleIdsByUgId(Integer ugId);
	
	//删除
	void deleteUgRoleByUgId(Integer ugId);
	
	//保存岗位与角色级联关系
	void saveUgRole(@Param("ugId") Integer ugId,@Param("roleId") Integer roleId);
	
	
}
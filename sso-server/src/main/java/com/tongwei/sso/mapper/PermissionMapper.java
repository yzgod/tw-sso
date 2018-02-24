package com.tongwei.sso.mapper;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.tongwei.auth.model.Permission;
import com.tongwei.common.dao.CmMapper;

/**
 * @author		yangz
 * @date		2018年1月17日 下午2:03:33
 * @description	权限
 */
public interface PermissionMapper extends CmMapper<Permission> {
	
	/** 根据角色ids集合查询权限Set */
	Set<Permission> queryPermsByRoleIds(@Param("ids") List<Integer> ids);
	
	/** 根据角色id查询权限Set */
	Set<Permission> queryPermsByRoleId(Integer roleId);
	
	List<Integer> getPermIdsByRoleIdAndAppCode(@Param("roleId") Integer roleId,@Param("appCode") String appCode);
	
}
package com.tongwei.sso.mapper;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.tongwei.auth.model.Menu;
import com.tongwei.common.dao.CmMapper;

/**
 * @author		yangz
 * @date		2018年1月17日 下午2:03:33
 * @description	菜单
 */
public interface MenuMapper extends CmMapper<Menu> {
	
	/** 根据角色id查询菜单Set */
	Set<Menu> queryMenusForRedisByRoleId(Integer roleId);
	
	List<Menu> getMenusByRoleIdAndAppCode(@Param("roleId") Integer roleId,@Param("appCode") String appCode);
	
}
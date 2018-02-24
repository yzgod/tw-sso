package com.tongwei.sso.mapper;

import com.tongwei.auth.model.UserGroup;
import com.tongwei.common.dao.CmMapper;

/**
 * @author		yangz
 * @date		2018年1月17日 下午2:03:33
 * @description	用户组
 */
public interface UserGroupMapper extends CmMapper<UserGroup> {
	
	
	UserGroup getUgById(Integer id);
	
}
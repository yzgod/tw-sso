package com.tongwei.sso.mapper;

import java.util.List;

import com.tongwei.auth.model.Org;
import com.tongwei.common.dao.CmMapper;

/**
 * @author		yangz
 * @date		2018年1月17日 下午2:03:33
 * @description 组织机构
 */
public interface OrgMapper extends CmMapper<Org> {
	
	List<Org> queryOrgByParentId(Integer parentId);
	
	Org getOrgById(Integer id);
	
	List<Org> getAllWithTypeName();
	
}
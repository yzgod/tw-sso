package com.tongwei.sso.mapper;

import java.util.List;

import com.tongwei.auth.model.Position;
import com.tongwei.common.dao.CmMapper;

/**
 * @author		yangz
 * @date		2018年1月17日 下午2:03:33
 * @description	岗位
 */
public interface PositionMapper extends CmMapper<Position> {
	
	List<Position> queryPositionsByOrgId(Integer oId);
	
	Position getPositionById(Integer id);
	
}
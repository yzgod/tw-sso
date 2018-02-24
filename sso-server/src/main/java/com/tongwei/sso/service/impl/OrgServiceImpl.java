package com.tongwei.sso.service.impl;

import org.springframework.stereotype.Service;

import com.tongwei.auth.model.Org;
import com.tongwei.common.dao.CmServiceImpl;
import com.tongwei.sso.service.IOrgService;

/**
 * @author		yangz
 * @date		2018年1月17日 下午3:16:59
 * @description	
 */
@Service
public class OrgServiceImpl extends CmServiceImpl<Org, Integer> implements IOrgService{

}

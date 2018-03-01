package com.tongwei.sso.service.impl;

import org.springframework.stereotype.Service;

import com.tongwei.auth.model.OrgType;
import com.tongwei.common.dao.CmServiceImpl;
import com.tongwei.sso.service.IOrgTypeService;

/**
 * @author yangz
 * @date 2018年1月17日 下午3:16:59
 */
@Service
public class OrgTypeServiceImpl extends CmServiceImpl<OrgType, Integer> implements IOrgTypeService {

}

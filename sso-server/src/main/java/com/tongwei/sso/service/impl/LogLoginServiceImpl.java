package com.tongwei.sso.service.impl;

import org.springframework.stereotype.Service;

import com.tongwei.common.dao.CmServiceImpl;
import com.tongwei.sso.model.LoginLog;
import com.tongwei.sso.service.ILogLoginService;

/**
 * @author		yangz
 * @date		2018年1月17日 下午3:16:59
 * @description	
 */
@Service
public class LogLoginServiceImpl extends CmServiceImpl<LoginLog, Integer> implements ILogLoginService{

}

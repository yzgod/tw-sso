package com.tongwei.sso.service.impl;

import org.springframework.stereotype.Service;

import com.tongwei.auth.log.DBUserLogBean;
import com.tongwei.common.dao.CmServiceImpl;
import com.tongwei.sso.service.ILogOpService;

/**
 * @author yangz
 * @date 2018年1月17日 下午3:16:59
 */
@Service
public class LogOpServiceImpl extends CmServiceImpl<DBUserLogBean, Integer> implements ILogOpService {

}

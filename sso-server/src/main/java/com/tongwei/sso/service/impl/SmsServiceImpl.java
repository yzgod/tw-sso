package com.tongwei.sso.service.impl;

import org.springframework.stereotype.Service;

import com.tongwei.aliyun.model.Sms;
import com.tongwei.common.dao.CmServiceImpl;
import com.tongwei.sso.service.ISmsService;

/**
 * @author yangz
 * @date 2018年1月17日 下午3:16:59
 */
@Service
public class SmsServiceImpl extends CmServiceImpl<Sms, Integer> implements ISmsService {

}

package com.tongwei.sso.service.impl;

import org.springframework.stereotype.Service;

import com.tongwei.auth.model.User;
import com.tongwei.common.dao.CmServiceImpl;
import com.tongwei.sso.service.IUserService;

/**
 * @author yangz
 * @date 2018年1月17日 下午3:16:59
 */
@Service
public class UserServiceImpl extends CmServiceImpl<User, Integer> implements IUserService {

}

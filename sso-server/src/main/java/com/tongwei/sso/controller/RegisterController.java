package com.tongwei.sso.controller;

import org.springframework.web.bind.annotation.PostMapping;

import com.tongwei.common.BaseController;
import com.tongwei.common.model.Result;
import com.tongwei.common.util.ResultUtil;

/**
 * @author yangz
 * @date 2018年3月1日 上午10:35:47
 * @description 用户注册
 */
public class RegisterController extends BaseController {

	// 用户注册
    @PostMapping(value = "/register")
    public Result register() throws Exception {
    	return ResultUtil.doFailure("按业务自行实现注册功能!");
    }
    
}

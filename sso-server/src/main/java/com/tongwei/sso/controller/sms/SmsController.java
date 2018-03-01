package com.tongwei.sso.controller.sms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tongwei.aliyun.model.Sms;
import com.tongwei.common.BaseController;
import com.tongwei.sso.query.SmsQuery;
import com.tongwei.sso.service.ISmsService;

/**
 * @author yangz
 * @date 2018年1月31日 下午2:34:14
 * @description 短信管理
 */
@RestController
@RequestMapping("/sms")
public class SmsController extends BaseController {

	@Autowired
	ISmsService smsService;

    @GetMapping("/query")
    public Object access(SmsQuery query) {
    	List<Sms> list = smsService.queryByPage(query);
        return renderPage(list, query);
    }

}

package com.tongwei.sso.controller.log;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tongwei.auth.log.DBAccessUserLogBean;
import com.tongwei.auth.log.DBUserLogBean;
import com.tongwei.common.BaseController;
import com.tongwei.sso.model.LoginLog;
import com.tongwei.sso.query.AccessLogQuery;
import com.tongwei.sso.query.LoginLogQuery;
import com.tongwei.sso.query.OpLogQuery;
import com.tongwei.sso.service.ILogAccessService;
import com.tongwei.sso.service.ILogLoginService;
import com.tongwei.sso.service.ILogOpService;
import com.tongwei.sso.service.IRegisterAppService;

/**
 * @author yangz
 * @date 2018年1月31日 下午2:34:14
 * @description 日志管理
 */
@RestController
@RequestMapping("/log")
public class LogController extends BaseController {

    @Autowired
    IRegisterAppService registerAppService;

    @Autowired
    ILogAccessService logAccessService;

    @Autowired
    ILogOpService logOpService;

    @Autowired
    ILogLoginService logLoginService;

    @GetMapping("/access/query")
    public Object access(AccessLogQuery query) {
        List<DBAccessUserLogBean> list = logAccessService.queryByPage(query);
        return renderPage(list, query);
    }

    @GetMapping("/op/query")
    public Object op(OpLogQuery query) {
        List<DBUserLogBean> list = logOpService.queryByPage(query);
        return renderPage(list, query);
    }

    @GetMapping("/login/query")
    public Object login(LoginLogQuery query) {
        List<LoginLog> list = logLoginService.queryByPage(query);
        return renderPage(list, query);
    }

}

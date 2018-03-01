package com.tongwei.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.tongwei.common.BaseController;
import com.tongwei.sso.util.SessionExUtil;

/**
 * @author yangz
 * @date 2018年1月31日 下午2:39:17
 * @description jsp视图
 */
@Controller
public class ViewController extends BaseController {

    // 主页
    @GetMapping("/")
    public String main() {
        request.setAttribute("onlineUsers", SessionExUtil.countUsers());
        request.setAttribute("onlineSessions", SessionExUtil.countSessions());
        return "main";
    }

    // 首页
    @GetMapping("/firstPage")
    public String firstPage() {
        return "firstPage";
    }

    // 用户
    @GetMapping("/user")
    public String user() {
        return "user/user";
    }

    // 用户组
    @GetMapping(value = "/ug")
    public String ug() {
        return "ug/ug";
    }

    // 角色
    @GetMapping("/role")
    public String role() {
        return "role/role";
    }

    // 岗位
    @GetMapping("/position")
    public String position() {
        return "position/position";
    }

    // 组织
    @GetMapping("/org")
    public String org() {
        return "org/org";
    }

    // 菜单
    @GetMapping("/menu")
    public String menu() {
        return "menu/menu";
    }

    // 权限
    @GetMapping("/perm")
    public String perm() {
        return "perm/perm";
    }

    // 用户角色授予
    @GetMapping("/role/user")
    public String role_user() {
        return "role/user_role";
    }

    // 组织角色授予
    @GetMapping("/role/org")
    public String org_role() {
        return "role/org_role";
    }

    // 岗位角色授予
    @GetMapping("/role/position")
    public String position_role() {
        return "role/position_role";
    }

    // 用户组角色授予
    @GetMapping("/role/ug")
    public String ug_role() {
        return "role/ug_role";
    }

    // 访问日志
    @GetMapping("/log/access")
    public String accessLog() {
        return "log/access";
    }

    // 操作日志
    @GetMapping("/log/op")
    public String opLog() {
        return "log/op";
    }

    // 登录日志
    @GetMapping("/log/lg")
    public String lg() {
        return "log/lg";
    }

    // 应用管理
    @GetMapping("/app/register")
    public String appRegister() {
        return "app/register";
    }
    
    // 短信管理
    @GetMapping("/sms")
    public String sms() {
    	return "sms/sms";
    }

}

package com.tongwei.sso;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tongwei.auth.anno.RequireRoles;
import com.tongwei.auth.log.AccessLog;
import com.tongwei.auth.util.AuthUtil;
import com.tongwei.auth.util.LogUtil;

/**
 * @author yangz
 * @date 2018年2月26日 上午10:23:46
 * @description AuthUtil,日志,鉴权 使用示例
 */
@RestController
@RequireRoles({ "system_sso", "system_cg" })
//@RequireUsers(value={"admin1","test"})
//@RequirePerms(value={"test1:dd","test3:test1","test2:test,test1"},requireType=RequireType.ALL)
public class QuickStartExample {

    String[] users = new String[] { "admin", "test" };

    @GetMapping(value = "/example")
    @AccessLog("测试")//访问日志
    public Object test(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("sessionId", request.getSession().getId());
        map.put("hasPerm", AuthUtil.hasPerm("testGroup", "testPerm"));
        map.put("hasRole", AuthUtil.hasRole("system_sso"));
        map.put("hasRoleTest", AuthUtil.hasRole("system_cg"));
        map.put("isUser", AuthUtil.isUser("admin"));
        map.put("isUsers", AuthUtil.isInUsers(users));
        map.put("menuTree", AuthUtil.getMenuTree());
        try {
            System.out.println(1 / 0);
        } catch (Exception e) {
            LogUtil.logUserToFile("哈哈哈哈", e);// 存入系统文件日志
        }
        LogUtil.logToDbOp("操作信息");//db操作日志
        return map;
    }

}
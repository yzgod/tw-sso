package com.tongwei.sso;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tongwei.auth.anno.RequireRoles;
import com.tongwei.auth.core.SessionCore;
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
public class QuickStartExample {

    String[] users = new String[] { "admin", "test" };
    
    @Value("${server.port}")
    String port;

    @GetMapping(value = "/example")
    @AccessLog("测试")//访问日志
    //@RequirePerms(value={"test1:dd","test3:test1","test2:test,test1"},requireType=RequireType.ALL)
    public Object test(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("sessionId", request.getSession().getId());
        map.put("hasPerm", AuthUtil.hasPerm("testGroup", "testPerm"));
        map.put("hasRole", AuthUtil.hasRole("system_sso"));
        map.put("hasRoleTest", AuthUtil.hasRole("system_cg"));
        map.put("isUser", AuthUtil.isUser("admin"));
        map.put("isUsers", AuthUtil.isInUsers(users));
        map.put("服务端口port:", port);
        map.put("menuTree", AuthUtil.getMenuTree());
        
        System.err.println("UserId: "+SessionCore.UserId.value());
        System.err.println("LoginName: "+SessionCore.LoginName.value());
        System.err.println("RealName: "+SessionCore.RealName.value());
        System.err.println("CellPhone: "+SessionCore.CellPhone.value());
        
        try {
            System.out.println(1 / 0);
        } catch (Exception e) {
            LogUtil.logUserToFile("哈哈哈哈", e);// 存入系统文件日志
        }
        LogUtil.logToDbOp("操作信息");//db操作日志
        return map;
    }

}
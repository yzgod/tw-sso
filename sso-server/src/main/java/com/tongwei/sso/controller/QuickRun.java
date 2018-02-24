package com.tongwei.sso.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tongwei.auth.anno.RequireRoles;
import com.tongwei.auth.log.AccessLog;
import com.tongwei.auth.util.AuthUtil;
import com.tongwei.auth.util.LogUtil;

@RestController
@RequireRoles({"system_sso","system_cg"})
//@RequireUsers(value={"admin1","test"})
//@RequirePerms(value={"test1:dd","test3:test1","test2:test,test1"},requireType=RequireType.ALL)
public class QuickRun {
	
	String[] users = new String[]{"admin","test"};
	
    @GetMapping(value = "/v")
    @AccessLog("测试")
    public Object test(HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        map.put("sessionId", request.getSession().getId());
        map.put("hasPerm", AuthUtil.hasPerm("testGroup","testPerm"));
        map.put("hasRole", AuthUtil.hasRole("system_sso"));
        map.put("hasRoleTest", AuthUtil.hasRole("system_cg"));
//        map.put("isUser", AuthUtil.isUser("admin"));
//        map.put("isUsers", AuthUtil.isInUsers(users));
        map.put("menuTree", AuthUtil.getMenuTree());
        try {
			System.out.println(1/0);
		} catch (Exception e) {
			LogUtil.logUserToFile("哈哈哈哈",e);//存入系统文件日志
		}
        LogUtil.logToDbOp("小明删除了一条关键信息,测试!");
        
        return map;
    }
    
}
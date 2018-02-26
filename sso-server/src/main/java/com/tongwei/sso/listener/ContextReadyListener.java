package com.tongwei.sso.listener;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.tongwei.auth.model.Role;
import com.tongwei.sso.mapper.RoleMapper;
import com.tongwei.sso.redis.RedisHandler;
import com.tongwei.sso.util.AuthService;

/**
 * @author		yangz
 * @date		2018年1月17日 下午4:43:02
 * @description	context准备完成事件
 * <p>1.处理所有权限/菜单加载到redis
 * 2.redis监听的job开始执行
 */
@Component
public class ContextReadyListener implements ApplicationListener<ApplicationReadyEvent> {
	
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		
		ApplicationContext ctx = event.getApplicationContext();
    	
    	//redis
        AuthService authService = ctx.getBean(AuthService.class);
        RoleMapper roleMapper = ctx.getBean(RoleMapper.class);
        
        //每个角色的菜单权限处理
        List<Role> roles = roleMapper.selectAll();
        for (Role role : roles) {
        	authService.syncRole(role,true);//强制同步所有角色
		}
        
        //job准备完成后执行
        Map<String, RedisHandler> beans = ctx.getBeansOfType(RedisHandler.class);
        Collection<RedisHandler> values = beans.values();
        for (RedisHandler handler : values) {
			handler.excute(ctx);
		}
	}
    
}
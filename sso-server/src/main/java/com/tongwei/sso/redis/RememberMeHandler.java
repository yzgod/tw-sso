package com.tongwei.sso.redis;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.tongwei.Const;
import com.tongwei.sso.util.AuthService;

/**
 * @author		yangz
 * @date		2018年2月2日 上午10:15:35
 * @description	记住登录用户缓存处理
 */
public class RememberMeHandler implements RedisHandler{
	
	private static Logger logger = LoggerFactory.getLogger(RememberMeHandler.class);
	
	private Thread thread;
	
	private boolean exit;//标识线程状态
	
	@Override
	public void excute(ApplicationContext ctx) {
		StringRedisTemplate redis = ctx.getBean(StringRedisTemplate.class);
		AuthService authService = ctx.getBean(AuthService.class);
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				BoundListOperations<String, String> ops = redis.boundListOps(Const.QUEUE_REMEMBER_ME_KEY);
				while (!exit) {
					String uidStr = ops.rightPop(1, TimeUnit.DAYS);
					if(uidStr==null){
						continue;
					}
					Integer userId = Integer.valueOf(uidStr);
					try {
						if(!exit){
							authService.syncAuthUser(userId);//同步
						}else{
							ops.rightPush(userId.toString());
						}
					}  catch (Exception e) {
						logger.error("rememberme handle error:",e);
					}
				}
			}
		};
		thread = new Thread(runnable,"rememberme");
		thread.start();
	}
	
	@Override
	public void destroy() {
		this.exit = true;
	}
	

}

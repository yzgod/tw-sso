package com.tongwei.sso.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.session.ExpiringSession;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.events.SessionDestroyedEvent;
import org.springframework.stereotype.Component;

import com.tongwei.Const;
import com.tongwei.auth.util.SessionUtil;
import com.tongwei.sso.util.SessionExUtil;

/**
 * @author		yangz
 * @date		2018年1月17日 下午4:43:02
 * @description	监听Session destroy
 */
@Component
public class SessionDestroyedListener implements ApplicationListener<SessionDestroyedEvent> {
	
	@Autowired
	RedisOperationsSessionRepository redis;
	
	@Override
	public void onApplicationEvent(SessionDestroyedEvent event) {
		ExpiringSession session = event.getSession();
		Integer userId = session.getAttribute(Const.SESSION_USER_ID);
		if(userId!=null){
//			System.err.println("destroy session");
			SessionExUtil.deleteSidFromMap(userId, session.getId());
			boolean b = SessionUtil.isOnlineUser(userId);
			if(b){//检查是否真的在线
				String[] sids = SessionExUtil.getSidsFromMap(userId);
				for (String sid : sids) {
					if(SessionUtil.isExpiredSessionId(sid)){//删除Map中过期的sessionId
						SessionExUtil.deleteSidFromMap(userId, sid);
					}
				}
			}
		}
	}
    
}
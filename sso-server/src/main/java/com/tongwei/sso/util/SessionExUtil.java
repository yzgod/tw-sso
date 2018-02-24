package com.tongwei.sso.util;

import java.util.Arrays;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.session.ExpiringSession;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.stereotype.Component;

import com.tongwei.Const;

/**
 * @author		yangz
 * @date		2018年2月7日 下午7:35:56
 * @description	
 */
@Component
public class SessionExUtil implements BeanFactoryAware{
	
	private static StringRedisTemplate redis;
	
	private static RedisOperationsSessionRepository rs;
	
	private static BoundHashOperations<String, String, String> ouMap;
	
	private static AuthService authService;
	
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		redis = beanFactory.getBean(StringRedisTemplate.class);
		rs = beanFactory.getBean(RedisOperationsSessionRepository.class);
		ouMap = redis.boundHashOps(Const.MAP_ONLINE_USER_SESSION_IDS);
		authService = beanFactory.getBean(AuthService.class);
	}
	
	//删除session
	public static void deleteSession(String sessionId){
		rs.delete(sessionId);
	}
	
	//根据sessionId获取session
	public static ExpiringSession getSession(String sessionId){
		return rs.getSession(sessionId);
	}
	
	/** 删除SessionId */
	public static void deleteSidFromMap(Integer userId,String sessionId) {
		String uId = userId.toString();
		Boolean hasKey = ouMap.hasKey(uId);
		if(hasKey){
			String sids = ouMap.get(uId);
			if(sids!=null){
				String replace = sids.replaceFirst(sessionId, "");
				if("".equals(replace)){
					ouMap.delete(uId);
					authService.deleteUser(userId);
				}else{
					ouMap.put(uId, replace);
				}
			}
		}
	}
	
	/** 获取SessionIds */
	public static String[] getSidsFromMap(Integer userId) {
		String uId = userId.toString();
		Boolean hasKey = ouMap.hasKey(uId);
		if(hasKey){
			String sids = ouMap.get(uId);
			int len = sids.length();
			if(len%36!=0){
				ouMap.delete(uId);
				throw new RuntimeException("sids长度不是36bit,已删除:"+sids);
			}
			int num = len/36;
			char[] cArr = sids.toCharArray();
			String[] s = new String[num];
			for (int i = 0; i < len/36; i++) {
				s[i] = String.valueOf(Arrays.copyOfRange(cArr, 36*i, 36*(i+1)));
			}
			return s;
		}
		return null;
	}
	
	//删除Map中的user
	public static void deleteMapUser(Integer userId){
		ouMap.delete(userId.toString());
	}
	
	/** 在线用户数量 */
	public static long countUsers(){
		return ouMap.size();
	}
	
	/** 在线session数量 */
	public static long countSessions(){
		Set<String> keys = redis.keys(Const.SPRING_SESSION_EXPIRE_PREFFIX+"*");
		return keys.size();
	}
	
}

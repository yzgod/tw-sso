package com.tongwei.auth.util;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.WebUtils;

import com.tongwei.Const;
import com.tongwei.auth.model.User;

/**
 * @author yangz
 * @date 2018年1月16日 下午3:01:27
 * @description 登录用户Session管理工具类
 */
public class SessionUtil implements BeanFactoryAware {

    private static final Logger log = LoggerFactory.getLogger(SessionUtil.class);

    private static StringRedisTemplate redis;

    private static BoundHashOperations<String, String, String> ou;

    /** 获取用户ID */
    public static Integer getUserId() {
        return (Integer) WebUtils.getSessionAttribute(getRequest(), Const.SESSION_USER_ID);
    }

    /** 获取用户登录名 */
    public static String getLoginName() {
        return (String) WebUtils.getSessionAttribute(getRequest(), Const.SESSION_USER_LOGINNAME);
    }

    /** 获取用户真实姓名 */
    public static String getRealName() {
        return (String) WebUtils.getSessionAttribute(getRequest(), Const.SESSION_USER_REALNAME);
    }

    /** 获取用户手机号 */
    public static String getCellPhone() {
        return (String) WebUtils.getSessionAttribute(getRequest(), Const.SESSION_USER_CELLPHONE);
    }

    /** 设置用户 */
    public static void setUser(User user) {
        if (user == null) {
            log.error("setUser failure the user is null.");
            return;
        }
        WebUtils.setSessionAttribute(getRequest(), Const.SESSION_USER_ID, user.getId());
        WebUtils.setSessionAttribute(getRequest(), Const.SESSION_USER_LOGINNAME, user.getLoginName());
        WebUtils.setSessionAttribute(getRequest(), Const.SESSION_USER_REALNAME, user.getRealName());
        WebUtils.setSessionAttribute(getRequest(), Const.SESSION_USER_CELLPHONE, user.getCellPhone());
        addToMap(user);
    }

    /** 设置当前session失效 */
    public static void invalidate() {
        getRequest().getSession().invalidate();
    }

    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /** 判断用户是否在线 */
    public static boolean isOnlineUser(Integer userId) {
        return ou.hasKey(userId.toString());
    }

    /** 记录用户登录后对应userId和SessionId的Map关系 */
    private static void addToMap(User user) {
        String uId = user.getId().toString();
        String sId = getRequest().getSession().getId();
        Boolean hasKey = ou.hasKey(uId);
        if (hasKey) {
            String sids = ou.get(uId);
            ou.put(uId, sids + sId);
        } else {
            ou.put(uId, sId);
        }
    }

    /** 判断sessionId是否过期 */
    public static boolean isExpiredSessionId(String sessionId) {
        return !redis.hasKey(Const.SPRING_SESSION_EXPIRE_PREFFIX + sessionId);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        redis = beanFactory.getBean(StringRedisTemplate.class);
        ou = redis.boundHashOps(Const.MAP_ONLINE_USER_SESSION_IDS);
    }

}
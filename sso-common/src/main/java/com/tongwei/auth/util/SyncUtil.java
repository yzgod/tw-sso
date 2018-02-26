package com.tongwei.auth.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.tongwei.Const;
import com.tongwei.auth.model.AuthUser;

/**
 * @author yangz
 * @date 2018年2月23日 上午9:52:02
 * @description 同步工具类
 */
public class SyncUtil implements BeanFactoryAware {

    private static Logger logger = LoggerFactory.getLogger(SyncUtil.class);

    private static StringRedisTemplate redis;

    // 向rememberme用户队列中添加指定需要同步的用户
    public static void addToRememberMeQueue(Integer userId) {
        redis.opsForList().leftPush(Const.QUEUE_REMEMBER_ME_KEY, userId.toString());
    }

    // 向rememberme用户队列中添加指定需要同步的用户并等待获取authUser
    public static AuthUser addToRememberMeQueueAndGetAuthUser(Integer userId) throws InterruptedException {
        addToRememberMeQueue(userId);
        Thread.sleep(1000);
        AuthUser authUser = AuthUtil.getAuthUser(userId);
        // check
        for (int i = 1; i <= 10; i++) {// 检查10次间隔1s
            Thread.sleep(1000);
            boolean existInRedis = SyncUtil.checkUserExistInRedis(userId);
            if (existInRedis) {
                authUser = AuthUtil.getAuthUser(userId);
                logger.info("check {} times found the userid:{}", i, userId);
                break;
            }
            if (!existInRedis && i == 10) {
                logger.error("userId:{}, Check the authuser 10 times but not found in redis", userId);
                return null;
            }
        }
        return authUser;
    }

    // 向角色队列中添加角色id同步
    public static void addToRoleQueue(Integer roleId) {
        redis.opsForList().leftPush(Const.QUEUE_ROLE_IDS_KEY, String.valueOf(roleId));
    }

    /** redis中是否包含用户 */
    public static boolean checkUserExistInRedis(Integer userId) {
        return redis.hasKey(Const.USER_PREFFIX + userId);
    }

    /** redis中是否存在角色 */
    public static boolean checkRoleExistInRedis(String appCode, Integer roleId) {
        return redis.hasKey(appCode + Const.ROLE_MIDDLE + roleId);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        redis = beanFactory.getBean(StringRedisTemplate.class);
    }

}

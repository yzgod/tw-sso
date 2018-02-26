package com.tongwei.sso.redis;

import org.springframework.context.ApplicationContext;

/**
 * @author yangz
 * @date 2018年2月3日 下午8:58:18
 * @description redis队列任务接口
 */
public interface RedisHandler {

    void excute(ApplicationContext ctx);

    void destroy();

}

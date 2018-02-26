package com.tongwei.sso.redis;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.tongwei.Const;
import com.tongwei.auth.model.Role;
import com.tongwei.sso.mapper.RoleMapper;
import com.tongwei.sso.util.AuthService;

/**
 * @author yangz
 * @date 2018年2月2日 上午10:15:35
 * @description 角色变化处理
 */
public class RoleChangeHandler implements RedisHandler {

    private static Logger logger = LoggerFactory.getLogger(RoleChangeHandler.class);

    private Thread thread;

    private boolean exit;// 标识线程状态

    @Override
    public void excute(ApplicationContext ctx) {
        RoleMapper roleMapper = ctx.getBean(RoleMapper.class);
        StringRedisTemplate redis = ctx.getBean(StringRedisTemplate.class);
        AuthService authService = ctx.getBean(AuthService.class);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                BoundListOperations<String, String> ops = redis.boundListOps(Const.QUEUE_ROLE_IDS_KEY);
                while (!exit) {
                    String roleIdStr = ops.rightPop(1, TimeUnit.DAYS);
                    if (roleIdStr == null) {
                        continue;
                    }
                    try {
                        if (!exit) {
                            Role role = roleMapper.selectByPrimaryKey(Integer.valueOf(roleIdStr));
                            authService.syncRole(role, true);
                        } else {
                            ops.rightPush(roleIdStr);
                        }
                    } catch (Exception e) {
                        logger.error("rolechange handle error:", e);
                    }
                }
            }
        };
        thread = new Thread(runnable, "roleChange");
        thread.start();
    }

    @Override
    public void destroy() {
        this.exit = true;
    }

}

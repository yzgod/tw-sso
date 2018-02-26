package com.tongwei.sso.redis;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tongwei.Const;
import com.tongwei.auth.log.DBAccessUserLogBean;
import com.tongwei.auth.log.DBUserLogBean;
import com.tongwei.auth.log.TextLogBean;
import com.tongwei.auth.log.TextUserLogBean;
import com.tongwei.sso.log.JobLogger;
import com.tongwei.sso.mapper.LogAccessMapper;
import com.tongwei.sso.mapper.LogOperateMapper;

/**
 * @author yangz
 * @date 2018年2月2日 上午10:15:35
 * @description 日志处理
 */
public class LogHandler implements RedisHandler {

    private static Logger logger = LoggerFactory.getLogger(LogHandler.class);

    private Thread thread;

    private boolean exit;// 标识线程状态

    @Override
    public void excute(ApplicationContext ctx) {
        StringRedisTemplate redis = ctx.getBean(StringRedisTemplate.class);
        LogAccessMapper logAccessMapper = ctx.getBean(LogAccessMapper.class);
        LogOperateMapper logOperateMapper = ctx.getBean(LogOperateMapper.class);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                BoundListOperations<String, String> ops = redis.boundListOps(Const.QUEUE_LOG_KEY);
                while (!exit) {
                    String logStr = ops.rightPop(1, TimeUnit.DAYS);
                    if (logStr == null) {
                        continue;
                    }
                    try {
                        if (!exit) {
                            JSONObject json = JSON.parseObject(logStr);
                            Integer type = json.getInteger("type");
                            switch (type) {
                            case 1:
                                DBUserLogBean dbUserLogBean = json.toJavaObject(DBUserLogBean.class);
                                // db操作
                                logOperateMapper.insert(dbUserLogBean);
                                break;
                            case 2:
                                DBAccessUserLogBean accessUserLogBean = json.toJavaObject(DBAccessUserLogBean.class);
                                // db操作
                                logAccessMapper.insert(accessUserLogBean);
                                break;
                            case 11:
                                TextLogBean textLogBean = json.toJavaObject(TextLogBean.class);
                                JobLogger.logDetail(textLogBean);
                                break;
                            case 12:
                                TextUserLogBean textUserLogBean = json.toJavaObject(TextUserLogBean.class);
                                JobLogger.logDetail(textUserLogBean);
                                break;
                            default:
                                break;
                            }
                        } else {
                            ops.rightPush(logStr);
                        }
                    } catch (Exception e) {
                        logger.error("logHandle error:", e);
                    }
                }
            }
        };
        thread = new Thread(runnable, "logHandler");
        thread.start();
    }

    @Override
    public void destroy() {
        this.exit = true;
    }

}

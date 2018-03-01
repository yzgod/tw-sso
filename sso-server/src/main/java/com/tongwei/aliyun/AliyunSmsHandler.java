package com.tongwei.aliyun;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.tongwei.aliyun.mapper.SmsMapper;
import com.tongwei.aliyun.model.Sms;
import com.tongwei.aliyun.sms.SmsUtil;
import com.tongwei.sso.redis.RedisHandler;

/**
 * @author yangz
 * @date 2018年2月2日 上午10:15:35
 * @description 短信处理
 */
public class AliyunSmsHandler implements RedisHandler {

    private static Logger logger = LoggerFactory.getLogger(AliyunSmsHandler.class);

    private Thread thread;

    private boolean exit;// 标识线程状态

    @Override
    public void excute(ApplicationContext ctx) {
        StringRedisTemplate redis = ctx.getBean(StringRedisTemplate.class);
        SmsMapper smsMapper = ctx.getBean(SmsMapper.class);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                BoundListOperations<String, String> ops = redis.boundListOps(AliyunConst.QUEUE_ALIYUN_SMS_KEY);
                while (!exit) {
                    String smsStr = ops.rightPop(1, TimeUnit.DAYS);
                    if (smsStr == null) {
                        continue;
                    }
                    try {
                        if (!exit) {
                            AliyunSms alisms = JSON.parseObject(smsStr, AliyunSms.class);
                            SendSmsResponse response = SmsUtil.sendAliyunSms(alisms);
                            if("OK".equals(response.getCode())){
                            	Sms sms = new Sms(alisms.getPhones(),alisms.getSignName(),alisms.getCode(),alisms.getParams(),alisms.getType());
                            	sms.setSendTime(new Date());
                            	smsMapper.insert(sms);
                            }else{
                            	logger.error("sms send failure response code:{} ,response msg:{}",response.getCode(),response.getMessage());
                            }
                        } else {
                            ops.rightPush(smsStr);
                        }
                    } catch (Exception e) {
                        logger.error("aliyun_sms handle error:", e);
                    }
                }
            }
        };
        thread = new Thread(runnable, "aliyun_sms");
        thread.start();
    }

    @Override
    public void destroy() {
        this.exit = true;
    }

}

package com.tongwei.aliyun;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.alibaba.fastjson.JSON;

/**
 * @author yangz
 * @date 2018年2月28日 下午5:16:23
 * @description 阿里云短信发送工具类
 */
public class AliyunSmsUtil implements BeanFactoryAware{
	
	private static StringRedisTemplate redis;
	
	private static BoundListOperations<String, String> lo;
	
	public static void send(AliyunSms aliyunSms){
		if(StringUtils.isBlank(aliyunSms.getPhones())){
			throw new IllegalArgumentException("电话号码不能为空!多个电话号码以,分割!");
		}
		if(StringUtils.isBlank(aliyunSms.getSignName())){
			throw new IllegalArgumentException("签名不能为空!");
		}
		if(StringUtils.isBlank(aliyunSms.getCode())){
			throw new IllegalArgumentException("短信模版编号不能为空!");
		}
		lo.leftPush(JSON.toJSONString(aliyunSms));
	}
	
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		redis = beanFactory.getBean(StringRedisTemplate.class);
		lo = redis.boundListOps(AliyunConst.QUEUE_ALIYUN_SMS_KEY);
	}
	
	
}

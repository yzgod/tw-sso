package com.tongwei.aliyun;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yangz
 * @date 2018年2月28日 下午4:31:48
 * @description 阿里云服务配置
 */
@Configuration
public class AliyunCommonConfiguration {
	
	@Bean
	public AliyunSmsUtil aliyunSmsUtil() {
		return new AliyunSmsUtil();
	}
}

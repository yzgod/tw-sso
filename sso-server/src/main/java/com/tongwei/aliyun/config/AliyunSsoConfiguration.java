package com.tongwei.aliyun.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tongwei.aliyun.AliyunEnv;
import com.tongwei.aliyun.AliyunSmsHandler;

/**
 * @author yangz
 * @date 2018年2月28日 下午4:31:48
 * @description 阿里云服务配置
 */
@Configuration
@ConfigurationProperties(prefix = "aliyun")
@MapperScan(basePackages = { "com.tongwei.aliyun.mapper" })
public class AliyunSsoConfiguration {
	
	public void setAccessKeyId(String accessKeyId) {
		AliyunEnv.ACCESSKEY_ID = accessKeyId;
	}
	public void setAccessKeySecret(String accessKeySecret) {
		AliyunEnv.ACCESSKEY_SECRET = accessKeySecret;
	}
	
	@Bean(destroyMethod = "destroy")
    public AliyunSmsHandler aliyunSmsHandler(ApplicationContext context) {
        return new AliyunSmsHandler();
    }
	
}

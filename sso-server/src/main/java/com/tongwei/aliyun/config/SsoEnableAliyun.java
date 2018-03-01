package com.tongwei.aliyun.config;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.tongwei.aliyun.AliyunCommonConfiguration;

/**
 * @author yangz
 * @date 2018年1月16日 下午3:50:30
 * @description 启用阿里云服务配置
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.TYPE })
@Documented
@Import({ AliyunSsoConfiguration.class,AliyunCommonConfiguration.class})
@Configuration
public @interface SsoEnableAliyun {

}

package com.tongwei.sso;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.tongwei.auth.anno.EnableSSO;

/**
 * @author yangz
 * @date 2018年1月16日 下午2:04:55
 * @description 认证端
 */
@SpringBootApplication
@EnableSSO(enableRememberMe = true, enableAccessLog = true, enableAuthAnnotation = true)
@EnableTransactionManagement
@MapperScan(basePackages = { "com.tongwei.sso.mapper" })
public class StartApp {

    public static void main(String[] args) {
        SpringApplication.run(StartApp.class, args);
    }

}

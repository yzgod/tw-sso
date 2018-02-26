package com.tongwei.auth.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import com.tongwei.auth.log.Log;

/**
 * @author yangz
 * @date 2018年2月6日 下午1:33:52
 * @description redis异步日志工具类
 */
public class LogUtil implements BeanFactoryAware {

    private static Log log;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        log = beanFactory.getBean(Log.class);
    }

    /** 用户访问信息保存 */
    public static void logToDbAccess(String msg, String method, String url, String ip, String parameter,
            Integer timeUsed) {
        log.logToDbAccess(msg, method, url, ip, parameter, timeUsed);
    }

    /** 用户操作信息保存 */
    public static void logToDbOp(String msg) {
        log.logToDbOp(msg);
    }

    /** 日志信息保存 */
    public static void logToFile(String msg) {
        log.logToFile(msg);
    }

    /** 日志信息保存,记录类名 */
    public static void logToFile(Class<?> clz, String msg) {
        log.logToFile(clz, msg);
    }

    /** 日志错误信息保存,记录异常 */
    public static void logToFile(String msg, Throwable error) {
        log.logToFile(msg, error);
    }

    /** 日志包含用户信息保存 */
    public static void logUserToFile(String msg) {
        log.logUserToFile(msg);
    }

    /** 日志包含用户信息保存,记录类名 */
    public static void logUserToFile(Class<?> clz, String msg) {
        log.logUserToFile(clz, msg);
    }

    /** 日志包含用户信息保存,记录异常 */
    public static void logUserToFile(String msg, Throwable error) {
        log.logUserToFile(msg, error);
    }

}

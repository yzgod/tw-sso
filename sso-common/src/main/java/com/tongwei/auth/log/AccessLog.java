package com.tongwei.auth.log;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author		yangz
 * @date		2018年2月5日 上午9:26:21
 * @description	访问日志注解
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.METHOD })
@Documented
@Inherited
public @interface AccessLog {

	/**
	 * 访问信息
	 */
	String value();

}

package com.tongwei.auth.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author yangz
 * @date 2018年2月5日 上午9:26:21
 * @description 指定用户loginNames访问
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.TYPE })
@Documented
@Inherited
public @interface RequireUsers {

    String[] value();

}

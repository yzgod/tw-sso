package com.tongwei.auth.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.tongwei.auth.security.RequireType;

/**
 * @author yangz
 * @date 2018年2月5日 上午9:26:21
 * @description 需要拥有权限
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.TYPE })
@Documented
@Inherited
public @interface RequirePerms {

    /** 格式permGroupCode1:perm1,perm2..., permGroupCode2:permA,permB... */
    String[] value();

    RequireType requireType() default RequireType.ANY;

}

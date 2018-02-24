package com.tongwei.auth.interceptors;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.tongwei.auth.anno.RequirePerms;
import com.tongwei.auth.anno.RequireRoles;
import com.tongwei.auth.anno.RequireUsers;
import com.tongwei.auth.helper.AuthValidateHelper;
import com.tongwei.auth.security.RequireType;
import com.tongwei.auth.util.AuthUtil;
import com.tongwei.common.util.ResponseUtil;

/**
 * @author		yangz
 * @date		2018年2月5日 上午9:22:57
 * @description	权限注解拦截器
 */
@SuppressWarnings("unchecked")
public class AuthorizationAttributeSourceInterceptor extends HandlerInterceptorAdapter {
	
	//权限注解
	private static final Class<? extends Annotation>[] AUTH_ANNOTATION_CLASSES =  
			new Class[] {RequireRoles.class,RequirePerms.class,RequireUsers.class};  
    
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();
		Annotation ma = getAuthAnnotation(method);
		if(ma==null){
			Annotation ca = getAuthAnnotation(method.getDeclaringClass());
			if(ca==null){
				return true;
			}
			if(validateAuth(ca)){
				return true;
			}
			ResponseUtil.responseUnAuthorizedJson(response);
			return false;
		}
		if(validateAuth(ma)){
			return true;
		}
		ResponseUtil.responseUnAuthorizedJson(response);
		return false;
	}
	
	private boolean validateAuth(Annotation a){
		if(a instanceof RequireRoles){//角色
			RequireType requireType = ((RequireRoles) a).requireType();
			String[] value = ((RequireRoles) a).value();
			if(requireType==RequireType.ANY){
				return AuthUtil.hasRolesAny(value);
			}
			if(requireType==RequireType.ALL){
				return AuthUtil.hasRolesAll(value);
			}
			return false;
		}
		if(a instanceof RequirePerms){//权限
			RequireType requireType = ((RequirePerms) a).requireType();
			String[] value = ((RequirePerms) a).value();
			if(requireType==RequireType.ANY){
				return AuthValidateHelper.hasPermAny(value);
			}
			if(requireType==RequireType.ALL){
				return AuthValidateHelper.hasPermAll(value);
			}
			return false;
		}
		if(a instanceof RequireUsers){//用户
			String[] value = ((RequireUsers) a).value();
			return AuthUtil.isInUsers(value);
		}
		return false;
	}

	
	private Annotation getAuthAnnotation(Class<?> clazz) {  
        for( Class<? extends Annotation> annClass : AUTH_ANNOTATION_CLASSES ) {  
            Annotation a = AnnotationUtils.findAnnotation(clazz, annClass);  
            if ( a != null ) {  
                return a;  
            }  
        }  
        return null;  
    }  
      
    private Annotation getAuthAnnotation(Method method) {  
        for( Class<? extends Annotation> annClass : AUTH_ANNOTATION_CLASSES ) {  
            Annotation a = AnnotationUtils.findAnnotation(method, annClass);  
            if ( a != null ) {  
                return a;  
            }  
        }  
        return null;  
    }  
	
}  
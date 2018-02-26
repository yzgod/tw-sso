package com.tongwei.auth.util;

import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import com.alibaba.fastjson.JSONArray;
import com.tongwei.auth.model.AuthUser;
import com.tongwei.auth.model.Menu;
import com.tongwei.auth.security.Auth;

/**
 * @author yangz
 * @date 2018年1月17日 下午4:25:21
 * @description 权限验证工具类
 */
public class AuthUtil implements BeanFactoryAware {

    private static Auth auth;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        auth = beanFactory.getBean(Auth.class);
    }

    /** 获取当前authUser对象 */
    public static AuthUser getAuthUser() {
        return auth.getAuthUser();
    }

    /** 根据用户id获取authUser对象,如不存在,返回null */
    public static AuthUser getAuthUser(Integer userId) {
        return auth.getAuthUser(userId);
    }

    /** 是否拥有角色 */
    public static boolean hasRole(String roleCode) {
        return auth.hasRole(roleCode);
    }

    /** 是否拥有角色组中的任一一个 */
    public static boolean hasRolesAny(String[] roleCodes) {
        return auth.hasRolesAny(roleCodes);
    }

    /** 是否拥有角色组中的所有角色 */
    public static boolean hasRolesAll(String[] roleCodes) {
        return auth.hasRolesAll(roleCodes);
    }

    /** 是否拥有权限 */
    public static boolean hasPerm(String permGroupCode, String permCode) {
        return auth.hasPerm(permGroupCode, permCode);
    }

    /** 是否拥有 任一权限 */
    public static boolean hasPermsAny(String permGroupCode, String[] permCodes) {
        return auth.hasPermsAny(permGroupCode, permCodes);
    }

    /** 是否拥有 所有权限 */
    public static boolean hasPermsAll(String permGroupCode, String[] permCodes) {
        return auth.hasPermsAll(permGroupCode, permCodes);
    }

    /** 是否属于组织架构 */
    public static boolean isInOrg(String orgCode) {
        return auth.isInOrg(orgCode);
    }

    /** 是否属于组织架构中的某类型指定组织,比如说部门,公司等 */
    public static boolean isInOrgType(String orgTypeCode) {
        return auth.isInOrgType(orgTypeCode);
    }

    /** 是否属于岗位 */
    public static boolean isInPosition(String positionCode) {
        return auth.isInPosition(positionCode);
    }

    /** 是否属于用户组 */
    public static boolean isInGroup(String groupCode) {
        return auth.isInGroup(groupCode);
    }

    /** 验证用户是否是指定账号用户 */
    public static boolean isUser(String loginName) {
        return auth.isUser(loginName);
    }

    /** 验证用户是否是指定账号s用户 */
    public static boolean isInUsers(String[] loginNames) {
        return auth.isInUsers(loginNames);
    }

    /** 获取当前用户当前应用菜单 */
    public static Set<Menu> getMenus() {
        return auth.getMenus();
    }

    /** 获取当前用户当前应用菜单树 */
    public static JSONArray getMenuTree() {
        return auth.getMenuTree();
    }

}

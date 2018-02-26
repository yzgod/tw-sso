package com.tongwei.auth.model;

import java.io.Serializable;
import java.util.Set;

/**
 * @author yangz
 * @date 2018年1月17日 下午4:15:40
 */
public class AuthRole implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 权限集合 */
    private Set<Permission> perms;

    /** 菜单集合 */
    private Set<Menu> menus;

    public Set<Permission> getPerms() {
        return perms;
    }

    public void setPerms(Set<Permission> perms) {
        this.perms = perms;
    }

    public Set<Menu> getMenus() {
        return menus;
    }

    public void setMenus(Set<Menu> menus) {
        this.menus = menus;
    }

}

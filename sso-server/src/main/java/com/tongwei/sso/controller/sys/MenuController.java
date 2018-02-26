package com.tongwei.sso.controller.sys;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tongwei.auth.model.Menu;
import com.tongwei.auth.model.Role;
import com.tongwei.common.BaseController;
import com.tongwei.common.model.Result;
import com.tongwei.common.util.ResultUtil;
import com.tongwei.sso.mapper.MenuMapper;
import com.tongwei.sso.mapper.RoleMapper;
import com.tongwei.sso.service.IMenuService;
import com.tongwei.sso.service.IRegisterAppService;
import com.tongwei.sso.util.AppUtils;
import com.tongwei.sso.util.AuthService;

/**
 * @author yangz
 * @date 2018年1月29日 下午12:16:29
 * @description 菜单管理
 */
@RestController
@RequestMapping("/menu")
public class MenuController extends BaseController {

    @Autowired
    IMenuService menuService;

    @Autowired
    IRegisterAppService registerAppService;

    @Autowired
    MenuMapper menuMapper;

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    AuthService authService;

    // 根据应用编码,查询该应用下的菜单树
    @GetMapping("/getMenuTreeByAppCode")
    public Object getMenuTreeByAppCode(String appCode) {
        List<Menu> list = menuService.findByPropAsc("appCode", appCode, "ord");
        if (list == null || list.isEmpty()) {
            return list;
        }
        List<Menu> rootMenus = list.stream().filter(e -> e.getParentId() == 0).collect(Collectors.toList());
        for (Menu menu : rootMenus) {
            recMenuTree(menu, list);
        }
        return rootMenus;
    }

    // 根据应用编码,父级菜单,父级菜单下的菜单
    @GetMapping("/getMenusByAppCodeAndParentId")
    public Object getMenuTreeByAppCode(String appCode, String pId) {
        List<Menu> list = menuService.findByPropAsc(new String[] { "appCode", "parentId" },
                new String[] { appCode, pId }, "ord");
        return list;
    }

    // 添加或修改菜单
    @PostMapping("/save")
    public Result save(Menu menu) {
        if (!AppUtils.validateCode(menu.getAppCode())) {
            return ResultUtil.doFailure("非法的appCode!");
        }
        if (menu.getParentId() == menu.getId()) {
            return ResultUtil.doFailure("非法的父菜单!");
        }
        boolean exist = registerAppService.checkIfExist("appCode", menu.getAppCode());
        if (!exist) {
            return ResultUtil.doFailure("非法的appCode!");
        }
        if (menu.getId() == null) {
            menuService.save(menu);
        } else {
            menu.setAppCode(null);
            menuService.updateNotNull(menu);
            // 计算菜单影响的角色,同步缓存
            List<Role> list = roleMapper.getRolesByMenuId(menu.getId());
            for (Role role : list) {
                authService.syncRole(role, true);
            }
        }
        return ResultUtil.doSuccess();
    }

    // 删除菜单
    @GetMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        // check菜单是否存在子菜单
        boolean exist = menuService.checkIfExist("parentId", id);
        if (exist) {
            return ResultUtil.doFailure("菜单存在子菜单,请先删除子级菜单!");
        }
        // check菜单是否授予角色
        List<Role> list = roleMapper.getRolesByMenuId(id);
        if (list.size() != 0) {
            return ResultUtil.doFailure("菜单已授于角色,请取消角色授予后再进行删除操作!");
        }
        menuService.delete(id);
        return ResultUtil.doSuccess("删除成功!");
    }

    // 根据id查询菜单
    @GetMapping("/get/{id}")
    public Menu get(@PathVariable Integer id) {
        Menu menu = menuService.get(id);
        return menu;
    }

    // 根据roleId,appCode查询所有菜单
    @GetMapping("/getMenusByRoleIdAndAppCode")
    public Object getMenusByRoleIdAndAppCode(@RequestParam String appCode, @RequestParam Integer roleId) {
        List<Menu> list = menuMapper.getMenusByRoleIdAndAppCode(roleId, appCode);
        return list;
    }

    private void recMenuTree(Menu menu, List<Menu> src) {
        List<Menu> children = src.stream().filter(e -> e.getParentId().equals(menu.getId()))
                .collect(Collectors.toList());
        for (Menu child : children) {
            List<Menu> cd = src.stream().filter(e -> e.getParentId().equals(child.getId()))
                    .collect(Collectors.toList());
            recMenuTree(child, src);
            child.setChildren(cd);
        }
        menu.setChildren(children);
    }

}

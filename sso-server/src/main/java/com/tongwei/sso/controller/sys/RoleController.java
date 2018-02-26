package com.tongwei.sso.controller.sys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tongwei.auth.model.AuthUser;
import com.tongwei.auth.model.Menu;
import com.tongwei.auth.model.Org;
import com.tongwei.auth.model.Position;
import com.tongwei.auth.model.Role;
import com.tongwei.auth.model.User;
import com.tongwei.auth.model.UserGroup;
import com.tongwei.auth.security.Auth;
import com.tongwei.auth.util.AuthUtil;
import com.tongwei.common.BaseController;
import com.tongwei.common.model.Result;
import com.tongwei.common.util.ResultUtil;
import com.tongwei.sso.mapper.OrgMapper;
import com.tongwei.sso.mapper.PositionMapper;
import com.tongwei.sso.mapper.RoleMapper;
import com.tongwei.sso.mapper.UserGroupMapper;
import com.tongwei.sso.mapper.UserMapper;
import com.tongwei.sso.model.RegisterApp;
import com.tongwei.sso.service.IMenuService;
import com.tongwei.sso.service.IOrgService;
import com.tongwei.sso.service.IPermissionService;
import com.tongwei.sso.service.IRegisterAppService;
import com.tongwei.sso.service.IRoleService;
import com.tongwei.sso.service.IUserService;
import com.tongwei.sso.util.AppUtils;
import com.tongwei.sso.util.AuthService;

/**
 * @author yangz
 * @date 2018年1月26日 下午2:44:28
 * @description 角色管理
 */
@RestController
@RequestMapping("/role")
public class RoleController extends BaseController {

    @Autowired
    AuthUtil su;

    @Autowired
    IRoleService roleService;

    @Autowired
    AuthService authService;

    @Autowired
    IRegisterAppService registerAppService;

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    OrgMapper orgMapper;

    @Autowired
    IMenuService menuService;

    @Autowired
    IPermissionService permissionService;

    @Autowired
    IUserService userService;

    @Autowired
    IOrgService orgService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    PositionMapper positionMapper;

    @Autowired
    UserGroupMapper userGroupMapper;

    @Autowired
    Auth auth;

    // 根据菜单的id查询拥有该菜单的 直接角色 列表
    @GetMapping("/getRolesByMenuId")
    public Object getRolesByMenuId(Integer mId) {
        List<Role> list = roleMapper.getRolesByMenuId(mId);
        return list;
    }

    // 根据权限的id查询拥有该权限的 直接角色 列表
    @GetMapping("/getRolesByPermId")
    public Object getRolesByPermId(Integer permId) {
        List<Role> list = roleMapper.getRolesByPermId(permId);
        return list;
    }

    // 根据appCode的查询所有角色
    @GetMapping("/getRolesByAppCode")
    public Object getRolesByAppCode(String appCode) {
        List<Role> list = roleService.findByProp("appCode", appCode);
        return list;
    }

    // 根据应用编码,父id,查询角色
    @GetMapping("/getRoleByParentId")
    public Object getRoleByParentId(String appCode, String pId) {
        List<Role> list = roleService.findByProp(new String[] { "appCode", "parentId" }, new String[] { appCode, pId });
        return list;
    }

    // 添加或修改组织
    @PostMapping("/save")
    public Result save(Role role) {
        Integer parentId = role.getParentId();
        if (parentId == null || parentId == 0) {
            role.setParentId(0);
        } else {
            boolean checkIfExist = roleService.checkIfExist(role.getParentId());
            if (!checkIfExist) {
                return ResultUtil.doFailure("父角色不存在!");
            }
        }

        if (role.getId() == null) {
            if (!AppUtils.validateCode(role.getAppCode())) {
                return ResultUtil.doFailure("非法的应用编码!");
            }
            if (!AppUtils.validateCode(role.getCode())) {
                return ResultUtil.doFailure("非法的编码!");
            }
            if (role.getCode().startsWith("system")) {
                return ResultUtil.doFailure("角色编码不能以system开头!");
            }
            boolean exist = registerAppService.checkIfExist("appCode", role.getAppCode());
            if (!exist) {
                return ResultUtil.doFailure("非法的appCode!");
            }
            boolean b = roleService.checkIfExist(new String[] { "appCode", "code" },
                    new Object[] { role.getAppCode(), role.getCode() });
            if (b) {
                return ResultUtil.doFailure("同一应用下,角色编码重复!");
            }
            roleService.save(role);
        } else {
            role.setAppCode(null);
            roleService.updateNotNull(role);
        }
        authService.addToRoleQueue(role.getId());// 缓存更新
        return ResultUtil.doSuccess("保存成功!");
    }

    // 删除角色
    @GetMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        Role role = roleService.get(id);
        if (role == null) {
            return ResultUtil.doFailure("角色不存在!");
        }
        String code = role.getCode();
        if (code.startsWith("system")) {
            return ResultUtil.doFailure("内置角色不允许直接删除!");
        }
        // check角色是否拥有权限,菜单
        boolean checkHasMenu = roleMapper.checkHasMenu(id);
        if (checkHasMenu) {
            return ResultUtil.doFailure("删除失败!角色拥有菜单,请取消关联!");
        }
        boolean checkHasPerm = roleMapper.checkHasPerm(id);
        if (checkHasPerm) {
            return ResultUtil.doFailure("删除失败!角色拥有权限,请取消关联!");
        }
        // check角色是否分配给用户,用户组,组织,岗位
        boolean checkBelongUser = roleMapper.checkBelongUser(id);
        if (checkBelongUser) {
            return ResultUtil.doFailure("删除失败!用户拥有该角色,请取消关联!");
        }
        boolean checkBelongUserGroup = roleMapper.checkBelongUserGroup(id);
        if (checkBelongUserGroup) {
            return ResultUtil.doFailure("删除失败!用户组拥有该角色,请取消关联!");
        }
        boolean checkBelongOrg = roleMapper.checkBelongOrg(id);
        if (checkBelongOrg) {
            return ResultUtil.doFailure("删除失败!组织架构拥有该角色,请取消关联!");
        }
        boolean checkBelongPosition = roleMapper.checkBelongPosition(id);
        if (checkBelongPosition) {
            return ResultUtil.doFailure("删除失败!岗位拥有该角色,请取消关联!");
        }
        roleService.delete(id);
        // 缓存清除
        authService.expireRole(role.getAppCode(), id);
        return ResultUtil.doSuccess("删除成功!");
    }

    // 保存菜单与角色的级联关系
    @PostMapping("/saveMenuRole")
    public Object saveMenuRole(@RequestParam Integer menuId, @RequestParam boolean isInclude,
            @RequestParam String roleIds) {
        if (StringUtils.isBlank(roleIds)) {
            return ResultUtil.doFailure("角色id不能为空!");
        }
        HashSet<Integer> roleIdSet = new HashSet<>();
        try {
            String[] split = roleIds.split(",");
            for (String id : split) {
                roleIdSet.add(Integer.valueOf(id));
            }
        } catch (Exception e) {
            return ResultUtil.doFailure("非法的角色id!");
        }

        Menu menu = menuService.get(menuId);
        if (menu == null) {
            return ResultUtil.doFailure("未查询到菜单!");
        }
        HashSet<Integer> menuIds = new HashSet<>();
        menuIds.add(menuId);
        if (isInclude) {// 查询所有子级菜单,孙子..
            String appCode = menu.getAppCode();
            List<Menu> src = menuService.findByProp("appCode", appCode);
            ArrayList<Menu> des = new ArrayList<>();
            recMenu(menu, src, des);
            for (Menu m : des) {// 过滤出menuIds
                if (appCode.equals(m.getAppCode())) {
                    menuIds.add(m.getId());
                }
            }
        }
        // 计算menuIds与roleIdSet
        for (Integer rId : roleIdSet) {
            for (Integer mId : menuIds) {
                try {
                    roleMapper.saveRoleMenu(rId, mId);
                } catch (Exception e) {
                    // 吃掉,会有重复的情况二报错
                }
            }
            authService.addToRoleQueue(rId);
        }
        return ResultUtil.doSuccess();
    }

    // 保存菜单与角色的级联关系
    @PostMapping("/savePermRole")
    public Object savePermRole(@RequestParam String permIds, @RequestParam String roleIds) {
        if (StringUtils.isBlank(roleIds)) {
            return ResultUtil.doFailure("角色id不能为空!");
        }
        if (StringUtils.isBlank(permIds)) {
            return ResultUtil.doFailure("权限id不能为空!");
        }

        HashSet<Integer> roleIdSet = new HashSet<>();
        try {
            String[] split = roleIds.split(",");
            for (String id : split) {
                roleIdSet.add(Integer.valueOf(id));
            }
        } catch (Exception e) {
            return ResultUtil.doFailure("非法的角色id!");
        }
        HashSet<Integer> permIdSet = new HashSet<>();
        try {
            String[] split = permIds.split(",");
            for (String id : split) {
                permIdSet.add(Integer.valueOf(id));
            }
        } catch (Exception e) {
            return ResultUtil.doFailure("非法的权限id!");
        }

        // 计算permIdSet与roleIdSet
        for (Integer rId : roleIdSet) {
            for (Integer pId : permIdSet) {
                try {
                    roleMapper.saveRolePerm(rId, pId);
                } catch (Exception e) {
                    // 吃掉,会有重复的情况报错
                }
            }
            authService.addToRoleQueue(rId);
        }
        return ResultUtil.doSuccess();
    }

    // 取消菜单与角色的级联关系
    @PostMapping("/deleteRoleMenu")
    public Object deleteRoleMenu(@RequestParam Integer menuId, @RequestParam String roleIds) {
        if (StringUtils.isBlank(roleIds)) {
            return ResultUtil.doFailure("角色id不能为空!");
        }
        HashSet<Integer> roleIdSet = new HashSet<>();
        ArrayList<String> list = new ArrayList<>();
        try {
            String[] split = roleIds.split(",");
            for (String id : split) {
                roleIdSet.add(Integer.valueOf(id));
                list.add(id);
            }
        } catch (Exception e) {
            return ResultUtil.doFailure("非法的角色id!");
        }

        boolean exist = menuService.checkIfExist(menuId);
        if (!exist) {
            return ResultUtil.doFailure("未查询到菜单!");
        }

        roleMapper.deleteRoleMenu(menuId, roleIdSet);
        authService.addToRoleQueue(list);
        return ResultUtil.doSuccess("取消成功");
    }

    // 取消菜单与角色的级联关系
    @PostMapping("/deleteRolePerm")
    public Object deleteRolePerm(@RequestParam Integer permId, @RequestParam String roleIds) {
        if (StringUtils.isBlank(roleIds)) {
            return ResultUtil.doFailure("角色id不能为空!");
        }
        HashSet<Integer> roleIdSet = new HashSet<>();
        ArrayList<String> list = new ArrayList<>();
        try {
            String[] split = roleIds.split(",");
            for (String id : split) {
                roleIdSet.add(Integer.valueOf(id));
                list.add(id);
            }
        } catch (Exception e) {
            return ResultUtil.doFailure("非法的角色id!");
        }

        boolean exist = permissionService.checkIfExist(permId);
        if (!exist) {
            return ResultUtil.doFailure("未查询到菜单!");
        }

        roleMapper.deleteRolePerm(permId, roleIdSet);
        authService.addToRoleQueue(list);
        return ResultUtil.doSuccess("取消成功");
    }

    // 获取完整的角色树-按应用区分
    @GetMapping("/getRoleTree")
    public Object getOrgTree() {
        boolean superAdmin = auth.isSuperAdmin();
        List<Role> list = roleMapper.selectAll();
        if (list == null || list.isEmpty()) {
            return list;
        }

        List<RegisterApp> apps = registerAppService.getAll();
        ArrayList<Object> data = new ArrayList<>();
        for (RegisterApp app : apps) {
            if (!superAdmin && "sso".equals(app.getAppCode())) {
                continue;
            }
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", 0 - app.getId());// 负数为了区分开应用和角色的id
            map.put("name", app.getName());
            map.put("appCode", app.getAppCode());
            List<Role> src = list.stream().filter(e -> e.getAppCode().equals(app.getAppCode()))
                    .collect(Collectors.toList());
            List<Role> rootRoles = src.stream().filter(e -> e.getParentId() == 0).collect(Collectors.toList());
            for (Role role : rootRoles) {
                recRoleTree(role, src);
            }
            map.put("children", rootRoles);
            data.add(map);
        }
        return data;
    }

    // 保存角色与菜单的级联关系
    @PostMapping("/saveRoleMenu")
    public Object saveRoleMenu(@RequestParam Integer roleId, @RequestParam String menuIds) {
        HashSet<Integer> menuIdSet = new HashSet<>();
        if (StringUtils.isNotBlank(menuIds)) {
            try {
                String[] split = menuIds.split(",");
                for (String id : split) {
                    menuIdSet.add(Integer.valueOf(id));
                }
            } catch (Exception e) {
                return ResultUtil.doFailure("非法的菜单id!");
            }
        }

        Role role = roleService.get(roleId);
        if (role == null) {
            return ResultUtil.doFailure("未查询到角色!");
        }
        roleMapper.deleteRoleMenuByRoleId(roleId);
        for (Integer mId : menuIdSet) {
            try {
                roleMapper.saveRoleMenu(roleId, mId);
            } catch (Exception e) {
                // 吃掉,如果重复的情况报错
            }
        }
        authService.addToRoleQueue(roleId);
        return ResultUtil.doSuccess("保存成功!");
    }

    // 保存角色与权限的级联关系
    @PostMapping("/saveRolePerm")
    public Object saveRolePerm(@RequestParam Integer roleId, @RequestParam String permIds) {
        HashSet<Integer> permIdSet = new HashSet<>();
        if (StringUtils.isNotBlank(permIds)) {
            try {
                String[] split = permIds.split(",");
                for (String id : split) {
                    permIdSet.add(Integer.valueOf(id));
                }
            } catch (Exception e) {
                return ResultUtil.doFailure("非法的菜单id!");
            }
        }

        Role role = roleService.get(roleId);
        if (role == null) {
            return ResultUtil.doFailure("未查询到角色!");
        }
        roleMapper.deleteRolePermByRoleId(roleId);
        for (Integer pId : permIdSet) {
            try {
                roleMapper.saveRolePerm(roleId, pId);
            } catch (Exception e) {
                // 吃掉,会有重复的情况二报错
            }
        }
        authService.addToRoleQueue(roleId);
        return ResultUtil.doSuccess("保存成功!");
    }

    // 根据用户id获取角色id集合
    @GetMapping("/getRoleIdsByUserId/{userId}")
    public Object getRoleIdsByUserId(@PathVariable Integer userId) {
        List<Integer> list = roleMapper.getRoleIdsByUserId(userId);
        return list;
    }

    // 保存用户与角色的级联关系
    @PostMapping("/saveUserRole")
    public Object saveUserRole(@RequestParam Integer userId, @RequestParam String roleIds) {
        HashSet<Integer> roleIdSet = new HashSet<>();
        if (StringUtils.isNotBlank(roleIds)) {
            try {
                String[] split = roleIds.split(",");
                for (String id : split) {
                    roleIdSet.add(Integer.valueOf(id));
                }
            } catch (Exception e) {
                return ResultUtil.doFailure("非法的角色id!");
            }

        }
        User user = userService.get(userId);
        if (user == null) {
            return ResultUtil.doFailure("未查询到用户!");
        }

        roleMapper.deleteUserRoleByUserId(userId);
        for (Integer rId : roleIdSet) {
            try {
                roleMapper.saveUserRole(userId, rId);
            } catch (Exception e) {
                // 吃掉,会有重复的情况二报错
            }
        }
        if (!user.getForbidden()) {
            boolean hasUser = authService.hasUser(userId);
            if (hasUser) {
                authService.addToUserQueue(userId);// user缓存更新
            }
        }
        return ResultUtil.doSuccess("保存成功!");
    }

    // 根据用户id获取角色id集合
    @GetMapping("/getRoleIdsByOrgId/{orgId}")
    public Object getRoleIdsByOrgId(@PathVariable Integer orgId) {
        List<Integer> list = roleMapper.getRoleIdsByOrgId(orgId);
        return list;
    }

    // 保存用户与角色的级联关系
    @PostMapping("/saveOrgRole")
    public Object saveOrgRole(@RequestParam Integer orgId, @RequestParam String roleIds) {
        HashSet<Integer> roleIdSet = new HashSet<>();
        if (StringUtils.isNotBlank(roleIds)) {
            try {
                String[] split = roleIds.split(",");
                for (String id : split) {
                    roleIdSet.add(Integer.valueOf(id));
                }
            } catch (Exception e) {
                return ResultUtil.doFailure("非法的角色id!");
            }
        }

        Org org = orgService.get(orgId);
        if (org == null) {
            return ResultUtil.doFailure("未查询到组织!");
        }

        roleMapper.deleteOrgRoleByOrgId(orgId);
        for (Integer rId : roleIdSet) {
            try {
                roleMapper.saveOrgRole(orgId, rId);
            } catch (Exception e) {
                // 吃掉,会有重复的情况二报错
            }
        }

        List<Org> list = orgMapper.selectAll();
        HashSet<Integer> orgIdSet = new HashSet<>();
        orgIdSet.add(orgId);// 父id
        List<Org> rootCh = list.stream().filter(e -> e.getParentId().equals(orgId)).collect(Collectors.toList());
        for (Org o : rootCh) {
            orgIdSet.add(o.getId());
            recOrg(o, list, orgIdSet);
        }

        List<User> users = userMapper.getUsersByOrgIds(orgIdSet);
        syncUsers(users);
        return ResultUtil.doSuccess("保存成功!");
    }

    // 根据岗位id获取角色id集合
    @GetMapping("/getRoleIdsByPositionId/{positionId}")
    public Object getRoleIdsByPositionId(@PathVariable Integer positionId) {
        List<Integer> list = roleMapper.getRoleIdsByPositionId(positionId);
        return list;
    }

    // 保存岗位与角色的级联关系
    @PostMapping("/savePositionRole")
    public Object savePositionRole(@RequestParam Integer positionId, @RequestParam String roleIds) {
        HashSet<Integer> roleIdSet = new HashSet<>();
        if (StringUtils.isNotBlank(roleIds)) {
            try {
                String[] split = roleIds.split(",");
                for (String id : split) {
                    roleIdSet.add(Integer.valueOf(id));
                }
            } catch (Exception e) {
                return ResultUtil.doFailure("非法的角色id!");
            }
        }

        Position position = positionMapper.selectByPrimaryKey(positionId);
        if (position == null) {
            return ResultUtil.doFailure("未查询到岗位!");
        }

        roleMapper.deletePositionRoleByPositionId(positionId);
        for (Integer rId : roleIdSet) {
            try {
                roleMapper.savePositionRole(positionId, rId);
            } catch (Exception e) {
                // 吃掉,会有重复的情况二报错
            }
        }

        List<Position> list = positionMapper.selectAll();
        HashSet<Integer> positionIdSet = new HashSet<>();
        positionIdSet.add(positionId);// 父id
        List<Position> rootCh = list.stream().filter(e -> e.getParentId().equals(positionId))
                .collect(Collectors.toList());
        for (Position o : rootCh) {
            positionIdSet.add(o.getId());
            recPos(o, list, positionIdSet);
        }

        List<User> users = userMapper.getUsersByPositionIds(positionIdSet);
        syncUsers(users);
        return ResultUtil.doSuccess("保存成功!");
    }

    // 根据用户组id获取角色id集合
    @GetMapping("/getRoleIdsByUgId/{ugId}")
    public Object getRoleIdsByUgId(@PathVariable Integer ugId) {
        List<Integer> list = roleMapper.getRoleIdsByUgId(ugId);
        return list;
    }

    // 保存用户组与角色的级联关系
    @PostMapping("/saveUgRole")
    public Object saveUgRole(@RequestParam Integer ugId, @RequestParam String roleIds) {
        HashSet<Integer> roleIdSet = new HashSet<>();
        if (StringUtils.isNotBlank(roleIds)) {
            try {
                String[] split = roleIds.split(",");
                for (String id : split) {
                    roleIdSet.add(Integer.valueOf(id));
                }
            } catch (Exception e) {
                return ResultUtil.doFailure("非法的角色id!");
            }
        }

        UserGroup ug = userGroupMapper.selectByPrimaryKey(ugId);
        if (ug == null) {
            return ResultUtil.doFailure("未查询到岗位!");
        }

        roleMapper.deleteUgRoleByUgId(ugId);
        for (Integer rId : roleIdSet) {
            try {
                roleMapper.saveUgRole(ugId, rId);
            } catch (Exception e) {
                // 吃掉,会有重复的情况二报错
            }
        }

        List<UserGroup> list = userGroupMapper.selectAll();
        HashSet<Integer> ugIdSet = new HashSet<>();
        ugIdSet.add(ugId);// 父id
        List<UserGroup> rootCh = list.stream().filter(e -> e.getParentId().equals(ugId)).collect(Collectors.toList());
        for (UserGroup o : rootCh) {
            ugIdSet.add(o.getId());
            recUg(o, list, ugIdSet);
        }

        List<User> users = userMapper.getUsersByUgIds(ugIdSet);
        syncUsers(users);
        return ResultUtil.doSuccess("保存成功!");
    }

    private void syncUsers(List<User> users) {
        for (User user : users) {
            if (!user.getForbidden()) {
                boolean hasUser = authService.hasUser(user.getId());
                if (hasUser) {
                    authService.addToUserQueue(user.getId());// user缓存更新
                }
            }
        }
    }

    private void recUg(UserGroup ug, List<UserGroup> src, Set<Integer> ids) {
        List<UserGroup> children = src.stream().filter(e -> e.getParentId().equals(ug.getId()))
                .collect(Collectors.toList());
        if (!children.isEmpty()) {
            children.stream().forEach(e -> {
                ids.add(e.getId());
                recUg(e, src, ids);
            });
        }
    }

    private void recOrg(Org org, List<Org> src, Set<Integer> ids) {
        List<Org> children = src.stream().filter(e -> e.getParentId().equals(org.getId())).collect(Collectors.toList());
        if (!children.isEmpty()) {
            children.stream().forEach(e -> {
                ids.add(e.getId());
                recOrg(e, src, ids);
            });
        }
    }

    private void recPos(Position pos, List<Position> src, Set<Integer> ids) {
        List<Position> children = src.stream().filter(e -> e.getParentId().equals(pos.getId()))
                .collect(Collectors.toList());
        if (!children.isEmpty()) {
            children.stream().forEach(e -> {
                ids.add(e.getId());
                recPos(e, src, ids);
            });
        }
    }

    private void recRoleTree(Role role, List<Role> src) {
        List<Role> children = src.stream().filter(e -> e.getParentId().equals(role.getId()))
                .collect(Collectors.toList());
        for (Role child : children) {
            List<Role> cd = src.stream().filter(e -> e.getParentId().equals(child.getId()))
                    .collect(Collectors.toList());
            recRoleTree(child, src);
            child.setChildren(cd);
        }
        role.setChildren(children);
    }

    private static void recMenu(Menu menu, List<Menu> src, List<Menu> des) {
        List<Menu> children = src.stream().filter(e -> e.getParentId().equals(menu.getId()))
                .collect(Collectors.toList());
        des.addAll(children);
        for (Menu m : children) {
            recMenu(m, src, des);
        }
    }

    // 管理员获取可以分配的角色
    @GetMapping("/getAssignRoles")
    public List<Object> getAssignRoles() {
        Set<Role> adminRoles = getAdminRoles();
        List<String> appCodes = new ArrayList<>();
        adminRoles.forEach(r -> appCodes.add(r.getAppCode()));
        boolean contains = appCodes.contains("sso");
        List<Object> datas = new ArrayList<>();
        if (contains) {// 所有角色可分配
            List<RegisterApp> list = registerAppService.getAll();
            for (RegisterApp registerApp : list) {
                datasInit(datas, registerApp);
            }
        } else {// 查询所属appCode的角色
            for (String appCode : appCodes) {
                RegisterApp one = registerAppService.getOne("appCode", appCode);
                if (one != null) {
                    datasInit(datas, one);
                }
            }
        }
        return datas;
    }

    private void datasInit(List<Object> datas, RegisterApp registerApp) {
        HashMap<String, Object> m = new HashMap<>(2);
        m.put("name", registerApp.getName());
        List<Role> roles = roleService.findByProp("appCode", registerApp.getAppCode());
        m.put("children", roles);
        datas.add(m);
    }

    private Set<Role> getAdminRoles() {
        AuthUser authUser = AuthUtil.getAuthUser();
        Set<Role> roles = authUser.getRoles();
        HashSet<Role> adminRoles = new HashSet<>();
        for (Role role : roles) {
            if ("system".equals(role.getCode())) {
                adminRoles.add(role);
            }
        }
        return adminRoles;
    }

}

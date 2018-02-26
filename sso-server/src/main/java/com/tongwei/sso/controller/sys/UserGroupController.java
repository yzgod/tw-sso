package com.tongwei.sso.controller.sys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tongwei.auth.model.Role;
import com.tongwei.auth.model.UserGroup;
import com.tongwei.common.BaseController;
import com.tongwei.common.model.Result;
import com.tongwei.common.util.ResultUtil;
import com.tongwei.sso.mapper.RegisterAppMapper;
import com.tongwei.sso.mapper.RoleMapper;
import com.tongwei.sso.mapper.UserGroupMapper;
import com.tongwei.sso.mapper.UserMapper;
import com.tongwei.sso.model.RegisterApp;
import com.tongwei.sso.service.IUserGroupService;
import com.tongwei.sso.service.IUserService;
import com.tongwei.sso.util.AppUtils;

/**
 * @author yangz
 * @date 2018年1月29日 下午12:16:29
 * @description 用户组管理
 */
@RestController
@RequestMapping("/ug")
public class UserGroupController extends BaseController {

    @Autowired
    UserMapper userMapper;

    @Autowired
    IUserService userService;

    @Autowired
    IUserGroupService userGroupService;

    @Autowired
    RegisterAppMapper registerAppMapper;

    @Autowired
    UserGroupMapper userGroupMapper;

    @Autowired
    RoleMapper roleMapper;

    // 根据分页高级查询
    @GetMapping("/getUGByParentId")
    public Object getUGByParentId(Integer pId) {
        List<UserGroup> list = userGroupService.findByPropAsc("parentId", pId, "ord");
        return list;
    }

    // 根据id获取详情
    @GetMapping(value = "/getUgById")
    public Result getUgById(Integer id) {
        UserGroup userGroup = userGroupService.get(id);
        return ResultUtil.doSuccess(userGroup);
    }

    // 添加或修改用户组
    @PostMapping("/save")
    public Result save(UserGroup ug) {
        if (ug.getParentId() == ug.getId()) {
            return ResultUtil.doFailure("非法的父用户组!");
        }
        if (ug.getId() == null) {
            if (!AppUtils.validateCode(ug.getCode())) {
                return ResultUtil.doFailure("非法的编码!");
            }
            userGroupService.save(ug);
        } else {
            ug.setCode(null);
            userGroupService.updateNotNull(ug);
        }
        return ResultUtil.doSuccess();
    }

    // 用户组编码重复验证,通过true,
    @GetMapping("/validateCode")
    public Boolean validateCode(String code) {
        boolean exist = userGroupService.checkIfExist("code", code);
        return !exist;
    }

    // 根据用户组id获取有效角色列表--有效角色,包含父组织的所有角色,角色的父角色
    @GetMapping("/queryRolesByUserGroupIds")
    public Result queryRolesByUserGroupIds(Integer ugId) {
        UserGroup userGroup = userGroupMapper.getUgById(ugId);
        HashSet<Integer> ids = new HashSet<>();
        recUg(userGroup, ids);
        List<RegisterApp> apps = registerAppMapper.selectAll();
        ArrayList<Object> data = new ArrayList<>();
        if (ids.isEmpty()) {
            for (RegisterApp app : apps) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("id", app.getId());
                map.put("name", app.getName());
                data.add(map);
            }
            return ResultUtil.doSuccess(data);
        }
        // 存在处理
        List<Role> roles = roleMapper.queryRolesByUserGroupIds(ids);
        // 角色计算/ 父角色 清算,保留不同id的父子角色
        HashSet<Role> cleanRoles = new HashSet<>();
        for (Role role : roles) {
            AppUtils.recursionRoles(cleanRoles, role);
        }
        for (RegisterApp app : apps) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", app.getId());
            map.put("name", app.getName());
            String appCode = app.getAppCode();
            List<Role> collect = cleanRoles.stream().filter(e -> appCode.equals(e.getAppCode()))
                    .collect(Collectors.toList());
            map.put("children", collect);
            data.add(map);
        }
        return ResultUtil.doSuccess(data);
    }

    // 获取完整的用户组树
    @GetMapping("/getUgTree")
    public List<UserGroup> getUgTree() {
        List<UserGroup> list = userGroupService.queryAsc("ord");
        if (list == null || list.isEmpty()) {
            return list;
        }
        List<UserGroup> rootUgs = list.stream().filter(e -> e.getParentId() == 0).collect(Collectors.toList());
        for (UserGroup ug : rootUgs) {
            recUgTree(ug, list);
        }
        return rootUgs;
    }

    private void recUgTree(UserGroup ug, List<UserGroup> src) {
        List<UserGroup> children = src.stream().filter(e -> e.getParentId().equals(ug.getId()))
                .collect(Collectors.toList());
        for (UserGroup child : children) {
            List<UserGroup> cd = src.stream().filter(e -> e.getParentId().equals(child.getId()))
                    .collect(Collectors.toList());
            recUgTree(child, src);
            child.setChildren(cd);
        }
        ug.setChildren(children);
    }

    private void recUg(UserGroup ug, Set<Integer> ids) {
        if (ug != null) {
            ids.add(ug.getId());
            UserGroup parentGroup = ug.getParentGroup();
            if (parentGroup != null) {
                recUg(parentGroup, ids);
            }
        }
    }

}

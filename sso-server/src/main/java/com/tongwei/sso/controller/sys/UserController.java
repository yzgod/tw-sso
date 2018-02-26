package com.tongwei.sso.controller.sys;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tongwei.auth.model.AuthUser;
import com.tongwei.auth.model.Role;
import com.tongwei.auth.model.User;
import com.tongwei.auth.util.SessionUtil;
import com.tongwei.common.BaseController;
import com.tongwei.common.model.Result;
import com.tongwei.common.util.ResultUtil;
import com.tongwei.sso.mapper.RegisterAppMapper;
import com.tongwei.sso.mapper.RoleMapper;
import com.tongwei.sso.mapper.UserMapper;
import com.tongwei.sso.model.LoginLog;
import com.tongwei.sso.model.RegisterApp;
import com.tongwei.sso.query.UserQuery;
import com.tongwei.sso.service.ILogLoginService;
import com.tongwei.sso.service.IUserService;
import com.tongwei.sso.util.AuthService;
import com.tongwei.sso.util.PasswordUtil;
import com.tongwei.sso.util.SessionExUtil;

/**
 * @author yangz
 * @date 2018年1月29日 下午12:16:29
 * @description 用户管理
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    UserMapper userMapper;

    @Autowired
    IUserService userService;

    @Autowired
    AuthService authService;

    @Autowired
    RegisterAppMapper registerAppMapper;

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    ILogLoginService logLoginService;

    // 根据分页高级查询
    @GetMapping(value = "/queryPage")
    public Object queryPage(UserQuery query) {
        List<User> list = userService.queryMultiByPage(query);
        return renderPage(list, query);
    }

    // 验证登录名是否重复
    @GetMapping("/validateLoginName")
    public Boolean validateLoginName(String loginName) {
        boolean exist = userService.checkIfExist("loginName", loginName);
        return !exist;
    }

    // 验证手机号码是否重复
    @GetMapping("/validateCellPhone")
    public Boolean validateCellPhone(String cellPhone) {
        boolean exist = userService.checkIfExist("cellPhone", cellPhone);
        return !exist;
    }

    // 获取用户有效的角色
    @GetMapping("/getAppRoleByUserId")
    public Result getAppRoleByUserId(Integer uId) {
        AuthUser authUser = userMapper.getAuthUser(uId);
        List<RegisterApp> list = registerAppMapper.selectAll();
        ArrayList<Object> arrayList = new ArrayList<>(list.size());
        if (authUser == null) {
            for (RegisterApp app : list) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("id", 0 - app.getId());
                map.put("name", app.getName());
                arrayList.add(map);
            }
            return ResultUtil.doSuccess(arrayList);
        }
        // 不为空
        authService.initRoles(authUser, false);
        Set<Role> roles = authUser.getRoles();
        List<Role> listRoles = roles.stream().sorted(Comparator.comparing(Role::getId)).collect(Collectors.toList());
        for (RegisterApp app : list) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", 0 - app.getId());
            map.put("name", app.getName());
            String appCode = app.getAppCode();
            List<Role> collect = listRoles.stream().filter(e -> appCode.equals(e.getAppCode()))
                    .collect(Collectors.toList());
            map.put("children", collect);
            arrayList.add(map);
        }
        return ResultUtil.doSuccess(arrayList);
    }

    // 获取用户详情
    @GetMapping(value = "/getDetail/{id}")
    public Object getDetail(@PathVariable Integer id) {
        User user = userService.get(id);
        if (user == null) {
            return ResultUtil.doFailure("用户不存在!");
        }
        String jsonString = JSON.toJSONString(user);
        JSONObject jsonObject = JSON.parseObject(jsonString);
        jsonObject.put("mainOrg", userMapper.getMainOrgId(id));
        jsonObject.put("partTimeOrgs", userMapper.getPartTimeOrgIds(id));
        jsonObject.put("mainPosition", userMapper.getMainPositionId(id));
        jsonObject.put("partTimePositions", userMapper.getPartTimePositionIds(id));
        jsonObject.put("userGroups", userMapper.getUgIds(id));
        return ResultUtil.doSuccess(jsonObject);
    }

    // 启用
    @GetMapping(value = "/enable/{id}")
    public Object enable(@PathVariable Integer id) {
        User user = userService.get(id);
        if (!user.getForbidden()) {
            return ResultUtil.doSuccess("该用户已启用登录!");
        }
        userService.updateByProp(id, "forbidden", false);
        return ResultUtil.doSuccess("启用登录成功!");
    }

    // 禁用
    @GetMapping(value = "/forbidden/{id}")
    public Object forbidden(@PathVariable Integer id) {
        User user = userService.get(id);
        if (user.getForbidden()) {
            return ResultUtil.doSuccess("该用户已禁止登录!");
        }
        AuthUser authUser = userMapper.getAuthUser(id);
        authService.initRoles(authUser, false);
        Set<Role> roles = authUser.getRoles();
        for (Role role : roles) {
            if ("system".equals(role.getCode())) {
                return ResultUtil.doFailure("拥有管理员角色的用户不允许被禁止登录!");
            }
        }
        userService.updateByProp(id, "forbidden", true);

        authService.deleteUser(user.getLoginName());// 删除缓存中的用户
        boolean onlineUser = SessionUtil.isOnlineUser(id);
        if (onlineUser) {
            SessionExUtil.deleteMapUser(id);
            String[] sids = SessionExUtil.getSidsFromMap(id);
            if (sids != null) {
                for (String sid : sids) {
                    SessionExUtil.deleteSession(sid);// 删除用户的所有sesion
                }
            }
            // 强制下线
            logLoginService.save(new LoginLog(user.getLoginName(), user.getRealName(), null, 3));
        }

        return ResultUtil.doSuccess("禁止登录成功!");
    }

    // 添加或修改用户
    @PostMapping("/save")
    public Result save(User user, Integer mainOrg, String partTimeOrgs, Integer mainPosition, String partTimePositions,
            String userGroups) throws Exception {

        HashSet<Integer> pOIds = new HashSet<>();
        if (StringUtils.isNotBlank(partTimeOrgs)) {
            String[] pOIdsStr = partTimeOrgs.split(",");
            for (String pOId : pOIdsStr) {
                pOIds.add(Integer.valueOf(pOId));
            }
        }
        if (pOIds.contains(mainOrg)) {
            return ResultUtil.doFailure("兼职组织包含主组织!");// 验证组织合理性
        }

        HashSet<Integer> ppIds = new HashSet<>();
        if (StringUtils.isNotBlank(partTimePositions)) {
            String[] ppIdsStr = partTimePositions.split(",");
            for (String ppId : ppIdsStr) {
                ppIds.add(Integer.valueOf(ppId));
            }
        }
        if (ppIds.contains(mainPosition)) {
            return ResultUtil.doFailure("兼职岗位中包含主岗位!");// 验证岗位
        }

        HashSet<Integer> ugIds = new HashSet<>();
        if (StringUtils.isNotBlank(userGroups)) {
            String[] ugIdsStr = userGroups.split(",");
            for (String ugId : ugIdsStr) {
                ugIds.add(Integer.valueOf(ugId));
            }
        }

        Integer id = user.getId();
        if (id == null) {// 添加
            user.setPassword(PasswordUtil.encodePwd("1234"));// 默认密码1234
            user.setCreateTime(new Date());
            userService.save(user);
        }

        // 关系维护
        Integer realUserId = user.getId();
        userMapper.deleteUserOrg(realUserId);
        if (mainOrg != null) {
            userMapper.saveUserOrg(realUserId, mainOrg, true);// 主组织
        }
        for (Integer poid : pOIds) {
            userMapper.saveUserOrg(realUserId, poid, false);// 兼职组织
        }

        userMapper.deleteUserPosition(realUserId);
        if (mainPosition != null) {
            userMapper.saveUserPosition(realUserId, mainPosition, true);// 主岗位
        }
        for (Integer ppid : ppIds) {
            userMapper.saveUserPosition(realUserId, ppid, false);// 兼职岗位
        }

        userMapper.deleteUserGroup(realUserId);
        for (Integer ugId : ugIds) {
            userMapper.saveUserGroup(realUserId, ugId);// 用户组
        }

        if (id != null) {// 编辑
            user.setCellPhone(null);
            user.setCreateTime(null);
            user.setForbidden(null);
            user.setLoginName(null);
            user.setPassword(null);
            userService.updateNotNull(user);
            // 缓存同步
            boolean hasUser = authService.hasUser(id);
            if (hasUser) {
                authService.syncAuthUser(id);
            }
        }
        return ResultUtil.doSuccess();
    }

}

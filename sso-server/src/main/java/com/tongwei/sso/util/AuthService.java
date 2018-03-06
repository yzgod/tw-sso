package com.tongwei.sso.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.tongwei.Const;
import com.tongwei.auth.core.SessionCore;
import com.tongwei.auth.model.AuthRole;
import com.tongwei.auth.model.AuthUser;
import com.tongwei.auth.model.Menu;
import com.tongwei.auth.model.Org;
import com.tongwei.auth.model.Permission;
import com.tongwei.auth.model.Position;
import com.tongwei.auth.model.Role;
import com.tongwei.auth.model.User;
import com.tongwei.auth.model.UserGroup;
import com.tongwei.auth.util.AESUtil;
import com.tongwei.auth.util.SessionUtil;
import com.tongwei.common.exception.AuthenticationExcption;
import com.tongwei.sso.mapper.LogLoginMapper;
import com.tongwei.sso.mapper.MenuMapper;
import com.tongwei.sso.mapper.PermissionMapper;
import com.tongwei.sso.mapper.RoleMapper;
import com.tongwei.sso.mapper.UserMapper;
import com.tongwei.sso.model.LoginLog;

/**
 * @author yangz
 * @date 2018年1月17日 下午4:25:21
 * @description 权限工具类
 */
@Service
@ConfigurationProperties(prefix = "sso.sys")
public class AuthService {

    /** aes加密的盐 */
    private String encodeSalt;

    @Autowired
    private StringRedisTemplate redis;

    @Autowired // 用户
    private UserMapper userMapper;

    @Autowired // 角色
    private RoleMapper roleMapper;

    @Autowired // 权限
    private PermissionMapper permissionMapper;

    @Autowired // 菜单
    private MenuMapper menuMapper;

    @Autowired
    LogLoginMapper logLoginMapper;

    public boolean hasUser(Integer userId) {
        return redis.hasKey(Const.USER_PREFFIX + userId);
    }

    /**
     * 将登陆用户 相关权限信息存入缓存
     * @throws Exception
     */
    public User login(String loginName, String password) throws Exception {
        password = PasswordUtil.encodePwd(password);
        User user = userMapper.login(loginName, password);
        if (user == null) {
            throw new AuthenticationExcption("用户名/密码不正确!");
        }
        HttpServletRequest request = SessionUtil.getRequest();
        SessionUtil.setUser(user);
        if (user.getForbidden()) {
            // 禁止登录日志
            LoginLog loginLog = new LoginLog(user.getLoginName(), user.getRealName(), request.getRemoteHost(), 2);
            logLoginMapper.insert(loginLog);
            throw new AuthenticationExcption("用户被禁止登陆");
        }
        // 用户登陆成功后强制同步用户信息
        AuthUser authUser = syncAuthUser(user.getId());
        Set<Role> roles = authUser.getRoles();
        for (Role role : roles) {
            syncRole(role);// 同步角色,不强制
        }
        // 登录成功
        LoginLog loginLog = new LoginLog(user.getLoginName(), user.getRealName(), request.getRemoteHost(), 1);
        logLoginMapper.insert(loginLog);
        return user;
    }

    public void loginout() {
    	HttpServletRequest request = SessionUtil.getRequest();
    	// 注销登录日志
    	LoginLog loginLog = new LoginLog(SessionCore.LoginName.value(), SessionCore.RealName.value(),
    			request.getRemoteHost(), 0);
        SessionUtil.invalidate();
        logLoginMapper.insert(loginLog);
    }

    // 同步AuthUser到redis
    public AuthUser syncAuthUser(Integer userId) {
        // 获取authuser
        AuthUser authUser = userMapper.getAuthUser(userId);
        initRoles(authUser);
        // 存入redis
        set(Const.USER_PREFFIX + userId, JSON.toJSONString(authUser));
        return authUser;
    }

    public void syncRole(Role role) {
        syncRole(role, false);
    }

    // 同步角色的权限和菜单
    public void syncRole(Role role, boolean force) {
        String appCode = role.getAppCode();
        String key = appCode + Const.ROLE_MIDDLE + role.getId();
        Boolean hasKey = redis.hasKey(key);
        if (force || !hasKey) {
            doSyncRole(key, role.getId());
        }
    }

    // 角色同步
    private void doSyncRole(String key, Integer roleId) {
        // 权限
        Set<Permission> perms = permissionMapper.queryPermsByRoleId(roleId);
        // 菜单-存在父级关系
        Set<Menu> menus = menuMapper.queryMenusForRedisByRoleId(roleId);
        AuthRole authRole = new AuthRole();
        authRole.setPerms(perms);
        authRole.setMenus(menus);
        // 存入redis
        set(key, JSON.toJSONString(authRole));
    }

    /**
     * 角色初始化, 组织,岗位,群组,人员
     */
    public void initRoles(AuthUser authUser) {
        initRoles(authUser, true);
    }

    public void initRoles(AuthUser authUser, boolean inRedis) {
        // role id相同认为是同一个role
        HashSet<Role> roles = new HashSet<>();

        // 组织
        List<Org> orgs = authUser.getOrgs();
        if (orgs != null && !orgs.isEmpty()) {// 如果用户属于组织
            Set<Integer> orgIds = new HashSet<>();
            for (Org org : orgs) {
                // 循环放入org id
                orgIds.add(org.getId());
                while (org.getParentOrg() != null) {
                    org = org.getParentOrg();
                    orgIds.add(org.getId());
                }
            }
            List<Role> orgRoles = roleMapper.queryRolesByOrgIds(orgIds);
            roles.addAll(orgRoles);
        }

        // 用户组
        List<UserGroup> groups = authUser.getGroups();
        if (groups != null && !groups.isEmpty()) {// 如果用户属于用户组
            Set<Integer> groupIds = new HashSet<>();
            for (UserGroup group : groups) {
                // 循环放入org id
                groupIds.add(group.getId());
                while (group.getParentGroup() != null) {
                    group = group.getParentGroup();
                    groupIds.add(group.getId());
                }
            }
            List<Role> groupRoles = roleMapper.queryRolesByUserGroupIds(groupIds);
            roles.addAll(groupRoles);
        }

        // 岗位
        List<Position> positions = authUser.getPositions();
        if (positions != null && !positions.isEmpty()) {
            Set<Integer> poIds = new HashSet<>();
            for (Position position : positions) {
                poIds.add(position.getId());
                while (position.getParentPosition() != null) {
                    position = position.getParentPosition();
                    poIds.add(position.getId());
                }
            }
            List<Role> poRoles = roleMapper.queryRolesByPositionIds(poIds);
            roles.addAll(poRoles);
        }

        // 人员本身的角色
        List<Role> userRoles = roleMapper.queryRolesByUserId(authUser.getUser().getId());
        if (userRoles != null && !userRoles.isEmpty()) {
            roles.addAll(userRoles);
        }

        // 角色计算/ 父角色 清算,保留不同id的父子角色
        HashSet<Role> cleanRoles = new HashSet<>();
        for (Role role : roles) {
            if (inRedis) {
                role.setName(null);
            }
            AppUtils.recursionRoles(cleanRoles, role);
        }
        authUser.setRoles(cleanRoles);
    }

    // 向用户队列中添加数据
    public void addToUserQueue(Integer userId) {
        redis.opsForList().leftPush(Const.QUEUE_USER_IDS_KEY, userId.toString());
    }

    // 向角色队列中添加数据
    public void addToRoleQueue(Integer roleId) {
        redis.opsForList().leftPush(Const.QUEUE_ROLE_IDS_KEY, String.valueOf(roleId));
    }

    // 向角色队列中添加数据
    public void addToRoleQueue(List<String> roleIds) {
        redis.opsForList().leftPushAll(Const.QUEUE_ROLE_IDS_KEY, roleIds);
    }

    public void expireRole(String appCode, Integer roleId) {
        redis.expire(appCode + Const.ROLE_MIDDLE + roleId, 1, TimeUnit.DAYS);// 设置1天后过期
    }

    // 删除缓存中的用户,相当于强制踢下线
    public void deleteUser(String loginName) {
        redis.delete(Const.USER_PREFFIX + loginName);
    }

    // 删除缓存中的用户,相当于强制踢下线
    public void deleteUser(Integer userId) {
        redis.delete(Const.USER_PREFFIX + userId);
    }

    // set
    private void set(String key, String value) {
        redis.opsForValue().set(key, value);
    }

    public void setEncodeSalt(String encodeSalt) {
        if (StringUtils.isBlank(encodeSalt)) {
            throw new IllegalArgumentException("应用间aes加密的盐值不能为空!");
        }
        if (this.encodeSalt == null) {// 第一次设置
            this.encodeSalt = encodeSalt;
            AESUtil.setEncodeSalt(encodeSalt);
            redis.opsForValue().set(Const.AES_SALT, encodeSalt);
        }
    }

}

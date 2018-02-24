package com.tongwei.auth.security;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.tongwei.Const;
import com.tongwei.auth.core.SessionCore;
import com.tongwei.auth.model.AuthRole;
import com.tongwei.auth.model.AuthUser;
import com.tongwei.auth.model.Menu;
import com.tongwei.auth.model.Org;
import com.tongwei.auth.model.OrgType;
import com.tongwei.auth.model.Permission;
import com.tongwei.auth.model.Position;
import com.tongwei.auth.model.Role;
import com.tongwei.auth.model.UserGroup;
import com.tongwei.auth.util.AESUtil;
import com.tongwei.auth.util.AuthUtil;
import com.tongwei.auth.util.SessionUtil;
import com.tongwei.auth.util.SyncUtil;
import com.tongwei.common.exception.CacheException;

/**
 * @author		yangz
 * @date		2018年2月6日 下午2:07:51
 * @description	提供权限验证
 */
public class Auth {

	private static final Logger logger = LoggerFactory.getLogger(AuthUtil.class);

	/** 当前应用的唯一编码*/
	private String appCode;
	
	@Autowired
	private StringRedisTemplate redis;
	
	/**
	 * 根据上下文获取用户信息
	 */
	public AuthUser getAuthUser() {
		//请求域中获取
		HttpServletRequest request = SessionUtil.getRequest();
		AuthUser rau = (AuthUser) request.getAttribute(Const.AUTH_USER_IN_REQUEST);
		if(rau!=null){
			return rau;
		}
		Integer userId = SessionCore.UserId.value();
		if(userId == null){
			logger.info("session user is null");
			return null;
		}
		AuthUser authUser = getAuthUser(userId);
		if(authUser!=null){
			request.setAttribute(Const.AUTH_USER_IN_REQUEST, authUser);
		}
		return authUser;
	}
	
	/** 根据用户id获取用户信息*/
	public AuthUser getAuthUser(Integer userId) {
		AuthUser authUser = getAuthUserFromRedis(userId);
		if(authUser==null){//从db中同步获取
			try {
				authUser = SyncUtil.addToRememberMeQueueAndGetAuthUser(userId);
			} catch (InterruptedException e) {
				logger.error("InterruptedException",e);
			}
		}
		return authUser;
	}
	
	/** 根据用户id获取用户信息-fromredis*/
	private AuthUser getAuthUserFromRedis(Integer userId) {
		boolean hasUser = checkUserExistInRedis(userId);
		if(!hasUser){
			return null;
		}
		String uStr = redis.opsForValue().get(Const.USER_PREFFIX+userId);
		AuthUser user = JSON.parseObject(uStr, AuthUser.class);
		return user;
	}
	
	/** 根据角色id获取角色信息
	 **/
	private AuthRole getAuthRole(Integer roleId) {
		AuthRole role = getAuthRoleFromRedis(roleId);
		if(roleId!=null && role==null){//有可能意外清除了角色
			SyncUtil.addToRoleQueue(roleId);//同步角色
			for (int i = 1; i <= 3; i++) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					logger.error("role InterruptedException:",e);
				}
				boolean existInRedis = SyncUtil.checkRoleExistInRedis(appCode, roleId);
				if(existInRedis){
					role = getAuthRoleFromRedis(roleId);
					logger.info("check {} times found the roleid:{}",i,roleId);
					break;
				}
				if(!existInRedis && i==3){
					logger.error("userId:{}, Check the authrole 3 times but not found in redis",roleId);
				}
			}
		}
		return role;
	}
	
	private AuthRole getAuthRoleFromRedis(Integer roleId){
		String pStr = redis.opsForValue().get(appCode+Const.ROLE_MIDDLE+roleId);
		return pStr==null?null:JSON.parseObject(pStr, AuthRole.class);
	}
	
	/** 判断用户是不是指定账号的用户 */
	public boolean isUser(String loginName){
		String loginName_ = SessionCore.LoginName.value();
		if(loginName.equals(loginName_)){
			return true;
		}
		return false;
	}
	
	/** 判断用户是不是指定账号s的用户 */
	public boolean isInUsers(String[] loginNames){
		String loginName_ = SessionCore.LoginName.value();
		if(loginName_!=null){
			for (String loginName : loginNames) {
				if(loginName.equals(loginName_)){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 验证用户是否拥有当前子应用 指定角色
	 */
	public boolean hasRole(String roleCode){
		AuthUser authUser = getAuthUser();
		Set<Role> roles = authUser.getRoles();
		if(roles==null || roles.isEmpty()){
			return false;
		}
		for (Role role : roles) {
			if(this.appCode.equals(role.getAppCode()) && roleCode.equals(role.getCode())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 验证用户是否拥有当前子应用 角色数组中的任一角色
	 */
	public boolean hasRolesAny(String[] roleCodes){
		AuthUser authUser = getAuthUser();
		Set<Role> roles = authUser.getRoles();
		if(roles==null || roles.isEmpty()){
			return false;
		}
		for (Role role : roles) {
			if(this.appCode.equals(role.getAppCode())){
				for (String roleCode : roleCodes) {
					if(roleCode.equals(role.getCode())){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * 验证用户是否拥有当前子应用 角色数组中的所有角色
	 */
	public boolean hasRolesAll(String[] roleCodes){
		int num = 0;
		AuthUser authUser = getAuthUser();
		Set<Role> roles = authUser.getRoles();
		if(roles==null || roles.isEmpty()){
			return false;
		}
		for (Role role : roles) {
			if(this.appCode.equals(role.getAppCode())){
				for (String roleCode : roleCodes) {
					if(roleCode.equals(role.getCode())){
						num++;
					}
				}
			}
		}
		return num==roleCodes.length;
	}
	
	/**
	 * 验证用户是否拥有当前子应用权限
	 * @param permGroupCode	应用权限组编码
	 * @param permCode		应用权限编码
	 * @return
	 */
	public boolean hasPerm(String permGroupCode,String permCode){
		AuthUser authUser = getAuthUser();
		Set<Role> roles = authUser.getRoles();
		if(roles==null || roles.isEmpty()){
			return false;
		}
		for (Role role : roles) {
			if(this.appCode.equals(role.getAppCode())){
				Integer id = role.getId();//roleId
				AuthRole authRole = getAuthRole(id);
				Set<Permission> perms = authRole.getPerms();
				for (Permission perm : perms) {
					String appCode_perm = perm.getAppCode();
					if(this.appCode.equals(appCode_perm) && perm.getGroupCode().equals(permGroupCode)){
						String code = perm.getCode();
						if(code.equals(permCode)){
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * 验证用户是否拥有当前子应用权限,任一权限
	 * @param permGroupCode	应用权限组编码
	 * @param permCode		应用权限编码
	 * @return
	 */
	public boolean hasPermsAny(String permGroupCode,String[] permCodes){
		AuthUser authUser = getAuthUser();
		Set<Role> roles = authUser.getRoles();
		if(roles==null || roles.isEmpty()){
			return false;
		}
		for (Role role : roles) {
			if(this.appCode.equals(role.getAppCode())){
				Integer id = role.getId();//roleId
				AuthRole authRole = getAuthRole(id);
				Set<Permission> perms = authRole.getPerms();
				for (Permission perm : perms) {
					String appCode_perm = perm.getAppCode();
					if(this.appCode.equals(appCode_perm) && perm.getGroupCode().equals(permGroupCode)){
						String code = perm.getCode();
						for (String permCode : permCodes) {
							if(code.equals(permCode)){
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * 验证用户是否拥有当前子应用权限,所有权限
	 * @param permGroupCode	应用权限组编码
	 * @param permCode		应用权限编码
	 * @return
	 */
	public boolean hasPermsAll(String permGroupCode,String[] permCodes){
		int num = 0; 
		AuthUser authUser = getAuthUser();
		Set<Role> roles = authUser.getRoles();
		if(roles==null || roles.isEmpty()){
			return false;
		}
		for (Role role : roles) {
			if(this.appCode.equals(role.getAppCode())){
				Integer id = role.getId();//roleId
				AuthRole authRole = getAuthRole(id);
				Set<Permission> perms = authRole.getPerms();
				if(perms!=null){
					for (Permission perm : perms) {
						String appCode_perm = perm.getAppCode();
						if(this.appCode.equals(appCode_perm) && perm.getGroupCode().equals(permGroupCode)){
							String code = perm.getCode();
							for (String permCode : permCodes) {
								if(code.equals(permCode)){
									num++;
								}
							}
						}
					}
				}
			}
		}
		return num==permCodes.length;
	}
	
	/**
	 * 验证用户是否属于组织架构中的某个指定组织
	 */
	public boolean isInOrg(String orgCode){
		AuthUser authUser = getAuthUser();
		List<Org> orgs = authUser.getOrgs();
		for (Org org : orgs) {
			if(recursionOrg(org,orgCode)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 验证用户是否属于组织架构中的某类型指定组织,比如说部门,公司等
	 */
	public boolean isInOrgType(String orgTypeCode){
		AuthUser authUser = getAuthUser();
		List<Org> orgs = authUser.getOrgs();
		for (Org org : orgs) {
			if(recTypeOrg(org, orgTypeCode)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 验证用户是否属于组织架构中的某个岗位
	 */
	public boolean isInPosition(String positionCode){
		AuthUser authUser = getAuthUser();
		List<Position> positions = authUser.getPositions();
		for (Position position : positions) {
			if(recursionPosition(position,positionCode)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 验证用户是否属于某个用户组
	 */
	public boolean isInGroup(String groupCode){
		AuthUser authUser = getAuthUser();
		List<UserGroup> groups = authUser.getGroups();
		for (UserGroup group : groups) {
			if(recursionGroup(group,groupCode)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 获取用户当前子应用菜单
	 */
	public Set<Menu> getMenus() {
		AuthUser authUser = getAuthUser();
		Set<Role> roles = authUser.getRoles();
		if(roles==null){
			return null;
		}
		Set<Menu> authMenus = new HashSet<>();
		for (Role role : roles) {
			String appCode_role = role.getAppCode();
			if(this.appCode.equals(appCode_role)){
				Integer id = role.getId();//roleId
				AuthRole authRole = getAuthRole(id);
				Set<Menu> menus = authRole.getMenus();
				authMenus.addAll(menus);
			}
		}
		return authMenus;
	}
	
	
	/**
	 * 获取用户当前子应用菜单树形结构,JsonStr
	 */
	public JSONArray getMenuTree(){
		AuthUser authUser = getAuthUser();
		Set<Role> roles = authUser.getRoles();
		if(roles==null){
			return new JSONArray();
		}
		
		Set<Menu> authMenus = new HashSet<>();
		for (Role role : roles) {
			String appCode_role = role.getAppCode();
			if(this.appCode.equals(appCode_role)){
				Integer id = role.getId();//roleId
				AuthRole authRole = getAuthRole(id);
				if(authRole!=null){
					Set<Menu> menus = authRole.getMenus();
					authMenus.addAll(menus);
				}
			}
		}
		
		ArrayList<Menu> sortedList = new ArrayList<>();
		sortedList.addAll(authMenus);
		//从小往大排序
		sortedList.sort((Menu m1,Menu m2)-> 
			{
				if(m1.getOrd()==null){
					return 1;
				}
				if(m2.getOrd()==null){
					return -1;
				}
				return m1.getOrd().compareTo(m2.getOrd());
			}
		);
		
		//根菜单必须存在
		List<Menu> rootMenus = sortedList.stream().filter(m->m.getParentId()==0).collect(Collectors.toList());
		for (Menu menu : rootMenus) {
			recursionMenus(menu,sortedList);
		}
		JSONArray arr = (JSONArray) JSON.toJSON(rootMenus);
		return arr;
	}
	
	//验证用户是否是本应用的管理员
	public boolean isThisAdmin(){
		AuthUser authUser = getAuthUser();
		Set<Role> roles = authUser.getRoles();
		if(roles==null || roles.isEmpty()){
			return false;
		}
		String thisAdminCode = "system_"+this.appCode;
		for (Role role : roles) {
			if(this.appCode.equals(role.getAppCode())){
				if(thisAdminCode.equals(role.getCode())){
					return true;
				}
			}
		}
		return false;
	}
	
	/** 验证用户是否是超级管理员*/
	public boolean isSuperAdmin(){
		AuthUser authUser = getAuthUser();
		Set<Role> roles = authUser.getRoles();
		if(roles==null || roles.isEmpty()){
			return false;
		}
		for (Role role : roles) {
			if("system_sso".equals(role.getCode())){
				return true;
			}
		}
		return false;
	}
	
	/** 验证用户是否是应用管理员 */
	public boolean isAppAdmin(){
		AuthUser authUser = getAuthUser();
		Set<Role> roles = authUser.getRoles();
		if(roles==null || roles.isEmpty()){
			return false;
		}
		for (Role role : roles) {
			if(role.getCode().startsWith("system_")){
				return true;
			}
		}
		return false;
	}
	
	/** 获取应用用户管理的应用的编码集合 */
	public List<String> getAppCodes(){
		AuthUser authUser = getAuthUser();
		Set<Role> roles = authUser.getRoles();
		List<String> list = new ArrayList<>();
		if(roles==null || roles.isEmpty()){
			return list;
		}
		for (Role role : roles) {
			if(role.getCode().startsWith("system_")){
				String appCode = role.getCode().substring(7);
				list.add(appCode);
			}
		}
		return list;
	}
	
	
	private boolean recursionOrg(Org org,String orgCode){
		String code = org.getCode();
		if(orgCode.equals(code)){
			return true;
		}
		Org parentOrg = org.getParentOrg();
		if(parentOrg!=null){
			return recursionOrg(parentOrg,orgCode);
		}
		return false;
	}
	
	private boolean recTypeOrg(Org org,String orgTypeCode){
		OrgType orgType = org.getOrgType();
		if(orgType!=null && orgTypeCode.equals(orgType.getCode())){
			return true;
		}
		Org parentOrg = org.getParentOrg();
		if(parentOrg!=null){
			return recursionOrg(parentOrg, orgTypeCode);
		}
		return false;
	}
	
	private boolean recursionPosition(Position position,String positionCode){
		String code = position.getCode();
		if(positionCode.equals(code)){
			return true;
		}
		Position parentPosition = position.getParentPosition();
		if(parentPosition!=null){
			return recursionPosition(parentPosition,positionCode);
		}
		return false;
	}
	
	private boolean recursionGroup(UserGroup group,String groupCode){
		String code = group.getCode();
		if(groupCode.equals(code)){
			return true;
		}
		UserGroup parentGroup = group.getParentGroup();
		if(parentGroup!=null){
			return recursionGroup(parentGroup,groupCode);
		}
		return false;
	}
	
	private void recursionMenus(Menu menu,List<Menu> sortedList){
		List<Menu> children = sortedList.stream().filter(m->menu.getId().equals(m.getParentId())).collect(Collectors.toList());
		if(!children.isEmpty()){
			for (Menu child : children) {
				recursionMenus(child,sortedList);
			}
			menu.setChildren(children);
		}
	}
	
	public boolean checkUserExistInRedis(Integer userId) {
		return redis.hasKey(Const.USER_PREFFIX+userId);
	}
	
	public HttpServletRequest getRequest(){
	    return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}
	
	//初始化操作
	public void noneCenterOperation(){
		Boolean hasKey = redis.hasKey(Const.AES_SALT);
		if(hasKey){
			String encodeSalt = redis.opsForValue().get(Const.AES_SALT);
			if(StringUtils.isNotBlank(encodeSalt)){
				AESUtil.setEncodeSalt(encodeSalt);
			}
		}else{
			throw new CacheException("缓存中不存在应用间aes加密的盐值!请确认认证中心已启动!");
		}
	}

	public void setAppCode(String appCode) {
		if(this.appCode==null){
			logger.info("应用appCode为:"+appCode);
			this.appCode = appCode;
		}else{
			logger.error("appcode="+this.appCode+",set twice is not successful");
		}
	}

	public String getAppCode() {
		return appCode;
	}
	
}

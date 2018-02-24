package com.tongwei.sso.controller.sys;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tongwei.auth.model.Permission;
import com.tongwei.auth.model.Role;
import com.tongwei.common.BaseController;
import com.tongwei.common.model.Result;
import com.tongwei.common.util.ResultUtil;
import com.tongwei.sso.mapper.PermGroupMapper;
import com.tongwei.sso.mapper.PermissionMapper;
import com.tongwei.sso.mapper.RoleMapper;
import com.tongwei.sso.model.PermGroup;
import com.tongwei.sso.service.IPermGroupService;
import com.tongwei.sso.service.IPermissionService;
import com.tongwei.sso.service.IRegisterAppService;
import com.tongwei.sso.util.ValidateUtil;

/**
 * @author		yangz
 * @date		2018年1月29日 下午12:16:29
 * @description	权限组
 */
@RestController
@RequestMapping("/perm")
public class PermController extends BaseController{
	
	@Autowired
	IRegisterAppService registerAppService;
	
	@Autowired
	IPermGroupService permGroupService;
	
	@Autowired
	PermGroupMapper permGroupMapper;
	
	@Autowired
	IPermissionService permissionService;
	
	@Autowired
	PermissionMapper permissionMapper;
	
	@Autowired
	RoleMapper roleMapper;
	
	
	//按照根据权限组id查询权限列表
	@GetMapping("/getPermsByPgId")
	public Object getPermsByPgId(Integer pgId){
		List<Permission> list = permissionService.findByPropAsc("groupId", pgId,"ord");
		return list;
	}
	
	//添加或修改权限
	@PostMapping("/save")
	public Result save(Permission perm){
		if(!ValidateUtil.validateCode(perm.getCode())){
			return ResultUtil.doFailure("非法的编码!");
		}
		if(perm.getId()==null){
			if(!ValidateUtil.validateCode(perm.getAppCode())){
				return ResultUtil.doFailure("非法的应用编码!");
			}
			boolean exist = registerAppService.checkIfExist("appCode", perm.getAppCode());
			if(!exist){
				return ResultUtil.doFailure("非法的appCode!");
			}
			Integer groupId = perm.getGroupId();
			if(groupId==0){
				return ResultUtil.doFailure("权限组不能为根权限组!");
			}
			PermGroup permGroup = permGroupService.get(groupId);
			if(permGroup==null || !perm.getAppCode().equals(permGroup.getAppCode())){
				return ResultUtil.doFailure("非法的权限组!");
			}
			boolean b = permissionService.checkIfExist(new String[]{"groupId","code"}, new Object[]{perm.getGroupId(),perm.getCode()});
			if(b){
				return ResultUtil.doFailure("同一权限组下,权限编码不能相同!");
			}
			perm.setGroupCode(permGroup.getCode());
			permissionService.save(perm);
		}else{
			perm.setAppCode(null);
			perm.setCode(null);
			perm.setGroupId(null);
			perm.setGroupCode(null);
			permissionService.updateNotNull(perm);
		}
		return ResultUtil.doSuccess("保存成功!");
	}
	
	//删除权限组
	@GetMapping("/delete/{id}")
	public Result delete(@PathVariable Integer id){
		//check权限是否授予了角色
		List<Role> list = roleMapper.getRolesByPermId(id);
		if(list.size()!=0){
			return ResultUtil.doFailure("权限已授于角色,请取消角色授予后再进行删除操作!");
		}
		permissionService.delete(id);
		return ResultUtil.doSuccess("删除成功!");
	}
	
	//获取权限tree,包含权限组
	@GetMapping("/getPermTree")
	public Object getPermTree(@RequestParam String appCode){
		List<PermGroup> pgs = permGroupService.findByPropAsc("appCode", appCode, "ord");
		for (PermGroup pg : pgs) {
			pg.setId(0 - pg.getId());//id设为负,区分权限和权限组
			pg.setParentId(0 - pg.getParentId());
		}
		List<PermGroup> roots = pgs.stream().filter(e->e.getParentId()==0).collect(Collectors.toList());
		for (PermGroup pg : roots) {
			recPermTree(pg, pgs);
		}
		return roots;
	}
	
	//获取有的权限id
	@GetMapping("/getPermIdsByRoleIdAndAppCode")
	public Object getPermsByRoleIdAndAppCode(@RequestParam String appCode,@RequestParam Integer roleId){
		List<Integer> list = permissionMapper.getPermIdsByRoleIdAndAppCode(roleId, appCode);
		return list;
	}
	
	
	
	private void recPermTree(PermGroup pg,List<PermGroup> src){
		List<PermGroup> children = src.stream().filter(e->e.getParentId().equals(pg.getId())).collect(Collectors.toList());
		for (PermGroup child : children) {
			List<PermGroup> cd = src.stream().filter(e->e.getParentId().equals(child.getId())).collect(Collectors.toList());
			recPermTree(child,src);
			List<Permission> childrenPerms = permissionService.findByProp("groupId", 0-child.getId());
			ArrayList<Object> cl = new ArrayList<>();
			cl.addAll(cd);
			cl.addAll(childrenPerms);
			child.setChildren(cl);
		}
		List<Permission> childrenPerms = permissionService.findByProp("groupId", 0-pg.getId());
		ArrayList<Object> cl = new ArrayList<>();
		cl.addAll(children);
		cl.addAll(childrenPerms);
		pg.setChildren(cl);
	}
	
}

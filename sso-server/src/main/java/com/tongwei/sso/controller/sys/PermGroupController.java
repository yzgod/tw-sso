package com.tongwei.sso.controller.sys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tongwei.auth.security.Auth;
import com.tongwei.common.BaseController;
import com.tongwei.common.model.Result;
import com.tongwei.common.util.ResultUtil;
import com.tongwei.sso.mapper.PermGroupMapper;
import com.tongwei.sso.model.PermGroup;
import com.tongwei.sso.model.RegisterApp;
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
@RequestMapping("/pg")
public class PermGroupController extends BaseController{
	
	@Autowired
	IRegisterAppService registerAppService;
	
	@Autowired
	IPermGroupService permGroupService;
	
	@Autowired
	PermGroupMapper permGroupMapper;
	
	@Autowired
	IPermissionService permissionService;
	
	@Autowired
	Auth auth;
	
	//按照应用区分查询权限组树
	@GetMapping("/getPermGroupTree")
	public Object getPermGroupTree(){
		boolean superAdmin = auth.isSuperAdmin();
		List<RegisterApp> apps = registerAppService.getAll();
		List<PermGroup> perms = permGroupService.queryAsc("ord");
		ArrayList<Object> data = new ArrayList<>();
		for (RegisterApp app : apps) {
			if(!superAdmin && "sso".equals(app.getAppCode())){
				continue;
			}
			HashMap<String, Object> map = new HashMap<>();
			map.put("id", 0 - app.getId());//负数为了区分开应用和权限组的id
			map.put("name", app.getName());
			map.put("appCode", app.getAppCode());
			List<PermGroup> src = perms.stream().filter(e->e.getAppCode().equals(app.getAppCode())).collect(Collectors.toList());
			List<PermGroup> roots = src.stream().filter(e->e.getParentId()==0).collect(Collectors.toList());
			for (PermGroup pg : roots) {
				recPgTree(pg, src);
			}
			map.put("children", roots);
			data.add(map);
		}
		return data;
	}
	
	//根据应用编码,父权限组id,查询子权限组
	@GetMapping("/getPgByParentId")
	public Object getPgByParentId(String appCode,String pId){
		List<PermGroup> list = permGroupService.findByPropAsc(new String[]{"appCode","parentId"} ,new String[]{appCode,pId}, "ord");
		return list;
	}
	
	//添加或修改权限组
	@PostMapping("/save")
	public Result save(PermGroup pg){
		if(!ValidateUtil.validateCode(pg.getAppCode())){
			return ResultUtil.doFailure("非法的应用编码!");
		}
		if(!ValidateUtil.validateCode(pg.getCode())){
			return ResultUtil.doFailure("非法的编码!");
		}
		if(pg.getParentId()==pg.getId()){
			return ResultUtil.doFailure("非法的父菜单!");
		}
		boolean exist = registerAppService.checkIfExist("appCode", pg.getAppCode());
		if(!exist){
			return ResultUtil.doFailure("非法的appCode!");
		}
		
		if(pg.getId()==null){
			boolean b = permGroupService.checkIfExist(new String[]{"appCode","code"}, new String[]{pg.getAppCode(),pg.getCode()});
			if(b){
				return ResultUtil.doFailure("同一应用下,权限组编码不能相同!");
			}
			permGroupService.save(pg);
		}else{
			pg.setAppCode(null);
			pg.setCode(null);
			permGroupService.updateNotNull(pg);
		}
		return ResultUtil.doSuccess("保存成功!");
	}
	
	//删除权限组
	@GetMapping("/delete/{id}")
	public Result delete(@PathVariable Integer id){
		//check权限组是否存在子菜单
		boolean exist = permGroupService.checkIfExist("parentId", id);
		if(exist){
			return ResultUtil.doFailure("权限组存在子权限组,请先删除子权限组!");
		}
		//check权限组是否包含权限
		boolean b = permissionService.checkIfExist("groupId", id);
		if(b){
			return ResultUtil.doFailure("权限组中存在权限,请先删除相关权限!");
		}
		permGroupService.delete(id);
		return ResultUtil.doSuccess("删除成功!");
	}
	
	
	
	private void recPgTree(PermGroup pg,List<PermGroup> src){
		List<PermGroup> children = src.stream().filter(e->e.getParentId().equals(pg.getId())).collect(Collectors.toList());
		for (PermGroup child : children) {
			List<PermGroup> cd = src.stream().filter(e->e.getParentId().equals(child.getId())).collect(Collectors.toList());
			recPgTree(child,src);
			child.setChildren(cd);
		}
		pg.setChildren(children);
	}
	
}

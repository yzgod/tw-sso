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

import com.tongwei.auth.model.Org;
import com.tongwei.auth.model.OrgType;
import com.tongwei.auth.model.Role;
import com.tongwei.common.BaseController;
import com.tongwei.common.model.Result;
import com.tongwei.common.util.ResultUtil;
import com.tongwei.sso.mapper.OrgMapper;
import com.tongwei.sso.mapper.OrgTypeMapper;
import com.tongwei.sso.mapper.RegisterAppMapper;
import com.tongwei.sso.mapper.RoleMapper;
import com.tongwei.sso.model.RegisterApp;
import com.tongwei.sso.service.IOrgService;
import com.tongwei.sso.util.AppUtils;
import com.tongwei.sso.util.ValidateUtil;

/**
 * @author		yangz
 * @date		2018年1月26日 下午2:44:28
 * @description	组织管理
 */
@RestController
@RequestMapping("/org")
public class OrgController extends BaseController{
	
	@Autowired
	IOrgService orgService;
	
	@Autowired
	OrgMapper orgMapper;

	@Autowired
	OrgTypeMapper orgTypeMapper;
	
	@Autowired
	RegisterAppMapper registerAppMapper;
	
	@Autowired
	RoleMapper roleMapper;

	
	//根据父id获取子列表
	@GetMapping("/getOrgByParentId")
	public Object getOrgByParentId(Integer pId){
		List<Org> list = orgMapper.queryOrgByParentId(pId);
		return list;
	}
	
	//根据id获取详情,包含父组织
	@GetMapping("/getOrgById")
	public Result getOrgById(Integer id){
		Org org = orgService.get(id);
		return ResultUtil.doSuccess(org);
	}
	
	//组织编码重复验证,通过true,
	@GetMapping("/validateCode")
	public Boolean validateCode(String code){
		boolean exist = orgService.checkIfExist("code", code);
		return !exist;
	}
	
	//添加或修改组织
	@PostMapping("/save")
	public Result save(Org org){
		if(!ValidateUtil.validateCode(org.getCode())){
			return ResultUtil.doFailure("非法的编码!");
		}
		if(org.getParentId()==org.getId()){
			return ResultUtil.doFailure("非法的父组织!");
		}
		if(org.getId()==null){
			orgService.save(org);
		}else{
			org.setCode(null);
			orgService.updateNotNull(org);
		}
		return ResultUtil.doSuccess();
	}
	
	//组织类别
	@GetMapping("/getOrgType")
	public Object getOrgType(){
		List<OrgType> list = orgTypeMapper.selectAll();
		return list;
	}
	
	
	//根据组织机构id获取有效角色列表--有效角色,包含父组织的所有角色,角色的父角色
	@GetMapping("/queryRolesByOrgId")
	public Result queryRolesByOrgId(Integer oId){
		Org org = orgMapper.getOrgById(oId);
		HashSet<Integer> ids = new HashSet<>();
		recOrg(org, ids);
		List<RegisterApp> apps = registerAppMapper.selectAll();
		ArrayList<Object> data = new ArrayList<>();
		if(ids.isEmpty()){
			for (RegisterApp app : apps) {
				HashMap<String, Object> map = new HashMap<>();
				map.put("id", app.getId());
				map.put("name", app.getName());
				data.add(map);
			}
			return ResultUtil.doSuccess(data);
		}
		//存在处理
		List<Role> roles = roleMapper.queryRolesByOrgIds(ids);
		// 角色计算/ 父角色 清算,保留不同id的父子角色
		HashSet<Role> cleanRoles = new HashSet<>();
		for (Role role : roles) {
			AppUtils.recursionRoles(cleanRoles,role);
		}
		for (RegisterApp app : apps) {
			HashMap<String, Object> map = new HashMap<>();
			map.put("id", app.getId());
			map.put("name", app.getName());
			String appCode = app.getAppCode();
			List<Role> collect = cleanRoles.stream().filter(e->appCode.equals(e.getAppCode())).collect(Collectors.toList());
			map.put("children", collect);
			data.add(map);
		}
		return ResultUtil.doSuccess(data);
	}
	
	//获取完整的组织机构树
	@GetMapping("/getOrgTree")
	public List<Org> getOrgTree(){
		List<Org> list = orgMapper.getAllWithTypeName();
		if(list==null || list.isEmpty()){
			return list;
		}
		List<Org> rootOrgs = list.stream().filter(e->e.getParentId()==0).collect(Collectors.toList());
		for (Org org : rootOrgs) {
			recOrgTree(org,list);
		}
		return rootOrgs;
	}
	
	private void recOrgTree(Org org,List<Org> src){
		List<Org> children = src.stream().filter(e->e.getParentId().equals(org.getId())).collect(Collectors.toList());
		for (Org child : children) {
			List<Org> cd = src.stream().filter(e->e.getParentId().equals(child.getId())).collect(Collectors.toList());
			recOrgTree(child,src);
			child.setChildren(cd);
		}
		org.setChildren(children);
	}
		
	private void recOrg(Org org,Set<Integer> ids){
		if(org!=null){
			ids.add(org.getId());
			Org parentOrg = org.getParentOrg();
			if(parentOrg!=null){
				recOrg(parentOrg,ids);
			}
		}
	}
	
}

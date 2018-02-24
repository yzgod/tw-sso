package com.tongwei.sso.controller.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tongwei.auth.model.Role;
import com.tongwei.auth.security.Auth;
import com.tongwei.common.BaseController;
import com.tongwei.common.model.Result;
import com.tongwei.common.util.ResultUtil;
import com.tongwei.sso.mapper.RoleMapper;
import com.tongwei.sso.model.RegisterApp;
import com.tongwei.sso.service.IMenuService;
import com.tongwei.sso.service.IPermGroupService;
import com.tongwei.sso.service.IPermissionService;
import com.tongwei.sso.service.IRegisterAppService;
import com.tongwei.sso.service.IRoleService;
import com.tongwei.sso.util.ValidateUtil;

/**
 * @author		yangz
 * @date		2018年1月31日 下午2:34:14
 * @description	应用管理
 */
@RestController
@RequestMapping("/app")
public class AppController extends BaseController{
	
	@Autowired
	IRegisterAppService registerAppService;
	
	@Autowired
	IRoleService roleService;
	
	@Autowired
	IMenuService menuService;
	
	@Autowired
	IPermissionService permissionService;
	
	@Autowired
	IPermGroupService permGroupService;
	
	@Autowired
	RoleMapper roleMapper;
	
	@Autowired
	Auth auth;
	
	@GetMapping("/getAll")
	public Object getAll(){
		boolean superAdmin = auth.isSuperAdmin();
		if(superAdmin){
			return registerAppService.getAll();
		}
		List<String> list = auth.getAppCodes();
		return registerAppService.findPropIn("appCode", list);
	}
	
	@GetMapping("/getByAppCode")
	public Object getByAppCode(String appCode){
		RegisterApp app = registerAppService.getOne("appCode", appCode);
		return app;
	}
	
	@GetMapping("/alert/{id}/{isEnable}")
	public Object enable(@PathVariable boolean isEnable,@PathVariable Integer id){
		registerAppService.updateByProp(id, "isAlert", isEnable);
		return ResultUtil.doSuccess(isEnable?"启用成功":"关闭成功");
	}
	
	//添加或修改app
	@PostMapping("/save")
	public Result save(RegisterApp app){
		if(app.getId()==null){
			String appCode = app.getAppCode();
			boolean b = ValidateUtil.validateCode(appCode);
			if(!b){
				return ResultUtil.doFailure("appCode不合法!");
			}
			boolean ifExist = registerAppService.checkIfExist("appCode", appCode);
			if(ifExist){
				return ResultUtil.doFailure("应用编码重复!");
			}
			//sso添加内置角色
			Role role = new Role();
			role.setAppCode("sso");
			role.setCode("system_"+appCode);
			role.setName("应用管理员角色-"+app.getName());
			role.setParentId(0);
			role.setRemark("应用管理员-内置角色");
			roleService.save(role);
			
			registerAppService.save(app);
		}else{
			app.setAppCode(null);
			registerAppService.updateNotNull(app);
		}
		return  ResultUtil.doSuccess("保存成功!");
	}
	
	//删除应用
	@GetMapping("/delete/{id}")
	public Result delete(@PathVariable Integer id){
		RegisterApp app = registerAppService.get(id);
		if(app==null){
			return ResultUtil.doFailure("应用不存在!");
		}
		int count = roleService.getCount("appCode",app.getAppCode());
		if(count>1){
			return ResultUtil.doFailure("应用下包含自定义角色,不允许删除!");
		}
		boolean menuExist = menuService.checkIfExist("appCode",app.getAppCode());
		if(menuExist){
			return ResultUtil.doFailure("应用下包含菜单,不允许删除!");
		}
		boolean pgExist = permGroupService.checkIfExist("appCode",app.getAppCode());
		if(pgExist){
			return ResultUtil.doFailure("应用下包含权限组,不允许删除!");
		}
		boolean pExist = permissionService.checkIfExist("appCode",app.getAppCode());
		if(pExist){
			return ResultUtil.doFailure("应用下包含权限,不允许删除!");
		}
		
		//验证内置角色关联问题
		Role role = roleService.getOne("code", "system_"+app.getAppCode());
		if(role==null){
			return ResultUtil.doFailure("系统异常,未查询到应用内置管理员角色!");
		}
		Integer roleId = role.getId();
		//check角色是否拥有权限,菜单
		boolean checkHasMenu = roleMapper.checkHasMenu(roleId);
		if(checkHasMenu){
			return ResultUtil.doFailure("删除失败!角色拥有菜单,请取消关联!");
		}
		boolean checkHasPerm = roleMapper.checkHasPerm(roleId);
		if(checkHasPerm){
			return ResultUtil.doFailure("删除失败!角色拥有权限,请取消关联!");
		}
		//check角色是否分配给用户,用户组,组织,岗位
		boolean checkBelongUser = roleMapper.checkBelongUser(roleId);
		if(checkBelongUser){
			return ResultUtil.doFailure("删除失败!用户拥有该角色,请取消关联!");
		}
		boolean checkBelongUserGroup = roleMapper.checkBelongUserGroup(roleId);
		if(checkBelongUserGroup){
			return ResultUtil.doFailure("删除失败!用户组拥有该角色,请取消关联!");
		}
		boolean checkBelongOrg = roleMapper.checkBelongOrg(roleId);
		if(checkBelongOrg){
			return ResultUtil.doFailure("删除失败!组织架构拥有该角色,请取消关联!");
		}
		boolean checkBelongPosition = roleMapper.checkBelongPosition(roleId);
		if(checkBelongPosition){
			return ResultUtil.doFailure("删除失败!岗位拥有该角色,请取消关联!");
		}
		roleService.delete(roleId);
		
		registerAppService.delete(id);
		return ResultUtil.doSuccess("删除成功!");
	}
	
	

}

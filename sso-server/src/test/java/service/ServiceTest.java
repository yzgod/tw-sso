package service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.tongwei.auth.log.Log;
import com.tongwei.sso.mapper.LogAccessMapper;
import com.tongwei.sso.mapper.LogOperateMapper;
import com.tongwei.sso.mapper.MenuMapper;
import com.tongwei.sso.mapper.OrgMapper;
import com.tongwei.sso.mapper.PermissionMapper;
import com.tongwei.sso.mapper.PositionMapper;
import com.tongwei.sso.mapper.RegisterAppMapper;
import com.tongwei.sso.mapper.RoleMapper;
import com.tongwei.sso.mapper.UserMapper;
import com.tongwei.sso.service.IUserService;
import com.tongwei.sso.util.AuthService;

import base.BaseTest;

/**
 * @author		yangz
 * @date		2018年1月17日 下午2:51:54
 * @description	
 */
public class ServiceTest extends BaseTest{
	
	@Autowired
	UserMapper userMapper;
	
	@Autowired
	OrgMapper orgMapper;
	
	@Autowired
	PositionMapper positionMapper;
	
	@Autowired
	RoleMapper roleMapper;
	
	@Autowired
	PermissionMapper permissionMapper;
	
	@Autowired
	MenuMapper menuMapper;
	
	@Autowired
	RegisterAppMapper registerAppMapper;
	
	@Autowired
	IUserService userService;
	
	@Autowired
	AuthService authService;
	
	@Autowired
	PositionMapper poMapper;
	
	@Autowired
	Log log;
	
	@Autowired
	LogAccessMapper accessMapper;
	
	@Autowired
	LogOperateMapper operateMapper;
	
	@Test
	public void testName() throws Exception {
		
		
		
	}
	

}

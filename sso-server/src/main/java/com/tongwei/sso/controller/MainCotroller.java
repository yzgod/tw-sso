package com.tongwei.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.tongwei.auth.core.SessionCore;
import com.tongwei.auth.model.User;
import com.tongwei.auth.util.AuthUtil;
import com.tongwei.common.BaseController;
import com.tongwei.common.model.Result;
import com.tongwei.common.util.ResultUtil;
import com.tongwei.sso.service.IUserService;
import com.tongwei.sso.util.PasswordUtil;

/**
 * @author yangz
 * @date 2018年1月26日 上午11:31:19
 * @description 首页
 */
@RestController
public class MainCotroller extends BaseController {

    @Autowired
    IUserService userService;

    // 获取菜单树
    @GetMapping(value = "/getMenuTree")
    public Result getMenuTree() {
        JSONArray menuTree = AuthUtil.getMenuTree();
        return ResultUtil.doSuccess(menuTree);
    }

    // 修改密码
    @PostMapping(value = "/changePwd")
    public Result changePwd(String oldPwd, String newPwd) throws Exception {
        Integer userId = SessionCore.UserId.value();
        if(userId==null){
        	return ResultUtil.doFailure("您未登录!");
        }
        String encodeOldPwd = PasswordUtil.encodePwd(oldPwd);
        User dbUser = userService.get(userId);
        String dbPwd = dbUser.getPassword();
        if (encodeOldPwd.equals(dbPwd)) {
            String encodePwd = PasswordUtil.encodePwd(newPwd);
            userService.updateByProp(userId, "password", encodePwd);
            return ResultUtil.doSuccess("密码修改成功!");
        }
        return ResultUtil.doFailure("原密码输入错误!");
    }
    

}

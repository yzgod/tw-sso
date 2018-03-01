package com.tongwei.sso.controller.sys;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tongwei.common.BaseController;
import com.tongwei.common.model.Result;
import com.tongwei.common.util.ResultUtil;
import com.tongwei.sso.model.BaseDept;
import com.tongwei.sso.model.BasePosition;
import com.tongwei.sso.query.BaseDeptQuery;
import com.tongwei.sso.query.BasePositionQuery;
import com.tongwei.sso.service.IBaseDeptService;
import com.tongwei.sso.service.IBasePositionService;

/**
 * @author yangz
 * @date 2018年1月26日 下午2:44:28
 * @description 基础部门岗位信息
 */
@RestController
@RequestMapping("/base")
public class BaseInfoController extends BaseController {

    @Autowired
    IBaseDeptService deptService;
    
    @Autowired
    IBasePositionService positionService;

    // 基础部门
    @GetMapping("/dept/query")
    public Object queryBaseDept(BaseDeptQuery query) {
        List<BaseDept> list = deptService.queryByPage(query);
        return renderPage(list, query);
    }
    
    // 添加或修改部门
    @PostMapping("/dept/save")
    public Result deptSave(BaseDept dept){
    	if(dept.getId() == null){//添加
    		boolean exist = deptService.checkIfExist("code", dept.getCode());
    		if(exist){
    			return ResultUtil.doFailure("部门编码重复!");
    		}
    		deptService.save(dept);
    	}else{//编辑
    		dept.setCode(null);
    		deptService.updateNotNull(dept);
    	}
		return ResultUtil.doSuccess();
    }
    
    // 基础岗位
    @GetMapping("/position/query")
    public Object queryBasePosition(BasePositionQuery query) {
    	List<BasePosition> list = positionService.queryByPage(query);
    	return renderPage(list, query);
    }
    
    // 添加或修改岗位
    @PostMapping("/position/save")
    public Result positionSave(BasePosition basePosition){
    	if(basePosition.getId() == null){//添加
    		boolean exist = positionService.checkIfExist("code", basePosition.getCode());
    		if(exist){
    			return ResultUtil.doFailure("岗位编码重复!");
    		}
    		positionService.save(basePosition);
    	}else{//编辑
    		basePosition.setCode(null);
    		positionService.updateNotNull(basePosition);
    	}
    	return ResultUtil.doSuccess();
    }
    

}

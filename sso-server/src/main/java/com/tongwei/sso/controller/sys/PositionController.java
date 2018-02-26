package com.tongwei.sso.controller.sys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tongwei.auth.model.Org;
import com.tongwei.auth.model.Position;
import com.tongwei.auth.model.Role;
import com.tongwei.common.BaseController;
import com.tongwei.common.model.Result;
import com.tongwei.common.util.ResultUtil;
import com.tongwei.sso.mapper.OrgMapper;
import com.tongwei.sso.mapper.PositionMapper;
import com.tongwei.sso.mapper.RegisterAppMapper;
import com.tongwei.sso.mapper.RoleMapper;
import com.tongwei.sso.model.RegisterApp;
import com.tongwei.sso.util.AppUtils;

/**
 * @author yangz
 * @date 2018年1月26日 下午2:44:28
 * @description 岗位管理
 */
@RestController
@RequestMapping("/position")
public class PositionController extends BaseController {

    @Autowired
    PositionMapper positionMapper;

    @Autowired
    RegisterAppMapper registerAppMapper;

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    OrgMapper orgMapper;

    // 获取组织下的岗位树
    @GetMapping("/getPostionTreeByOrgId")
    public Result getPostionTreeByOrgId(Integer oId) {
        List<Position> list = positionMapper.queryPositionsByOrgId(oId);
        if (list == null || list.isEmpty()) {
            return ResultUtil.doSuccess(list);
        }
        List<Position> rootPos = list.stream().filter(e -> e.getParentId() == 0).collect(Collectors.toList());
        for (Position pos : rootPos) {
            recPosTree(pos, list);
        }
        return ResultUtil.doSuccess(rootPos);
    }

    // 获取组织架构及岗位
    @GetMapping("/getOrgPostionTree")
    public Result getOrgPostionTree() {
        List<Org> list = orgMapper.getAllWithTypeName();

        if (list == null || list.isEmpty()) {
            return ResultUtil.doSuccess();
        }
        List<Org> rootOrgs = list.stream().filter(e -> e.getParentId() == 0).collect(Collectors.toList());
        for (Org org : rootOrgs) {
            recOrgTree(org, list);
            org.setId(0 - org.getId());
        }

        return ResultUtil.doSuccess(rootOrgs);
    }

    private void recOrgTree(Org org, List<Org> src) {
        List<Object> children = src.stream().filter(e -> e.getParentId().equals(org.getId()))
                .collect(Collectors.toList());
        for (Object obj : children) {
            Org child = (Org) obj;
            recOrgTree(child, src);
            child.setId(0 - child.getId());
        }

        List<Position> list = positionMapper.queryPositionsByOrgId(org.getId());
        if (list != null && !list.isEmpty()) {
            List<?> rootPos = list.stream().filter(e -> e.getParentId() == 0).collect(Collectors.toList());
            for (Object o : rootPos) {
                Position pos = (Position) o;
                recPosTree(pos, list);
                children.add(pos);
            }
        }
        org.setChildren(children);
    }

    // 根据岗位id获取有效角色列表--有效角色,包含父岗位的所有角色,角色的父角色
    @GetMapping("/queryRolesByPosId")
    public Result queryRolesByPosId(Integer posId) {
        Position position = positionMapper.getPositionById(posId);
        HashSet<Integer> ids = new HashSet<>();
        recPos(position, ids);
        List<RegisterApp> apps = registerAppMapper.selectAll();
        ArrayList<Object> data = new ArrayList<>();
        if (ids.isEmpty()) {
            for (RegisterApp app : apps) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("id", app.getId());
                map.put("name", app.getName());
                data.add(map);
            }
            return ResultUtil.doSuccess(data);
        }
        // 存在处理
        List<Role> roles = roleMapper.queryRolesByPositionIds(ids);
        // 角色计算/ 父角色 清算,保留不同id的父子角色
        HashSet<Role> cleanRoles = new HashSet<>();
        for (Role role : roles) {
            AppUtils.recursionRoles(cleanRoles, role);
        }
        for (RegisterApp app : apps) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", app.getId());
            map.put("name", app.getName());
            String appCode = app.getAppCode();
            List<Role> collect = cleanRoles.stream().filter(e -> appCode.equals(e.getAppCode()))
                    .collect(Collectors.toList());
            map.put("children", collect);
            data.add(map);
        }
        return ResultUtil.doSuccess(data);
    }

    private void recPos(Position position, Set<Integer> ids) {
        if (position != null) {
            ids.add(position.getId());
            Position parentPosition = position.getParentPosition();
            if (parentPosition != null) {
                recPos(parentPosition, ids);
            }
        }
    }

    private void recPosTree(Position pos, List<Position> src) {
        List<Position> children = src.stream().filter(e -> e.getParentId().equals(pos.getId()))
                .collect(Collectors.toList());
        for (Position child : children) {
            List<Position> cd = src.stream().filter(e -> e.getParentId().equals(child.getId()))
                    .collect(Collectors.toList());
            recPosTree(child, src);
            child.setChildren(cd);
        }
        pos.setChildren(children);
    }

}

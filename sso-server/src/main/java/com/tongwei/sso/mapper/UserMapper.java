package com.tongwei.sso.mapper;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.tongwei.auth.model.AuthUser;
import com.tongwei.auth.model.User;
import com.tongwei.common.dao.CmMapper;

/**
 * @author yangz
 * @date 2018年1月17日 下午2:03:33
 * @description 用户
 */
public interface UserMapper extends CmMapper<User> {

    /** 根据用户id获取authUser */
    AuthUser getAuthUser(Integer userId);

    /** 登陆验证 */
    User login(@Param("loginName") String loginName, @Param("password") String password);

    // 根据组织id获取用户
    List<User> getUserByOrgId(Integer orgId);

    List<User> getUsersByOrgIds(@Param("orgIds") Set<Integer> orgIds);

    List<User> getUsersByPositionIds(@Param("positionIds") Set<Integer> positionIds);

    List<User> getUsersByUgIds(@Param("ugIds") Set<Integer> ugIds);

    void saveUserOrg(@Param("userId") Integer userId, @Param("orgId") Integer orgId,
            @Param("isDefault") boolean isDefault);

    void deleteUserOrg(Integer userId);

    void saveUserPosition(@Param("userId") Integer userId, @Param("positionId") Integer positionId,
            @Param("isDefault") boolean isDefault);

    void deleteUserPosition(Integer userId);

    void saveUserGroup(@Param("userId") Integer userId, @Param("ugId") Integer ugId);

    void deleteUserGroup(Integer userId);

    Integer getMainOrgId(Integer userId);

    List<Integer> getPartTimeOrgIds(Integer userId);

    Integer getMainPositionId(Integer userId);

    List<Integer> getPartTimePositionIds(Integer userId);

    List<Integer> getUgIds(Integer userId);

}
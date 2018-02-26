package com.tongwei.sso.query;

import org.apache.commons.lang.StringUtils;

import com.tongwei.common.dao.BaseQuery;
import com.tongwei.sso.model.LoginLog;
import com.tongwei.sso.util.DateUtils;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * @author yangz
 * @date 2018年1月29日 下午12:43:19
 * @description 登录日志分页高级查询
 */
public class LoginLogQuery extends BaseQuery<LoginLog> {
    private static final long serialVersionUID = 1L;

    private String loginName;

    private String startDate;

    private String endDate;

    @Override
    public Example getExample() {
        // 配置Example
        this.example = new Example(entityClass);
        example.setOrderByClause("create_date desc");
        Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(loginName)) {
            criteria.andEqualTo("loginName", loginName);
        }
        if (StringUtils.isNotBlank(startDate)) {
            criteria.andGreaterThan("createDate", startDate);
        }
        if (StringUtils.isNotBlank(endDate)) {
            criteria.andLessThan("createDate", DateUtils.addDays(DateUtils.parseDate(endDate), 1));
        }
        return example;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

}

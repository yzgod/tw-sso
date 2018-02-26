package com.tongwei.sso.query;

import org.apache.commons.lang.StringUtils;

import com.tongwei.auth.log.DBAccessUserLogBean;
import com.tongwei.common.dao.BaseQuery;
import com.tongwei.sso.util.DateUtils;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * @author yangz
 * @date 2018年1月29日 下午12:43:19
 * @description 访问日志分页高级查询
 */
public class AccessLogQuery extends BaseQuery<DBAccessUserLogBean> {
    private static final long serialVersionUID = 1L;

    private String appCode;

    private String startDate;

    private String endDate;

    @Override
    public Example getExample() {
        // 配置Example
        this.example = new Example(entityClass);
        example.setOrderByClause("create_date desc");
        Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(appCode)) {
            criteria.andEqualTo("appCode", appCode);
        }
        if (StringUtils.isNotBlank(startDate)) {
            criteria.andGreaterThan("date", startDate);
        }
        if (StringUtils.isNotBlank(endDate)) {
            criteria.andLessThan("date", DateUtils.addDays(DateUtils.parseDate(endDate), 1));
        }
        return example;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
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

package com.tongwei.sso.query;

import org.apache.commons.lang.StringUtils;

import com.tongwei.aliyun.model.Sms;
import com.tongwei.common.dao.BaseQuery;
import com.tongwei.sso.util.DateUtils;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * @author yangz
 * @date 2018年1月29日 下午12:43:19
 * @description 短信记录分页高级查询
 */
public class SmsQuery extends BaseQuery<Sms> {
    private static final long serialVersionUID = 1L;

    private String code;

    private String startDate;

    private String endDate;

    @Override
    public Example getExample() {
        // 配置Example
        this.example = new Example(entityClass);
        example.setOrderByClause("send_time desc");
        Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(code)) {
            criteria.andEqualTo("code", code);
        }
        if (StringUtils.isNotBlank(startDate)) {
            criteria.andGreaterThan("sendTime", startDate);
        }
        if (StringUtils.isNotBlank(endDate)) {
            criteria.andLessThan("sendTime", DateUtils.addDays(DateUtils.parseDate(endDate), 1));
        }
        return example;
    }

    public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

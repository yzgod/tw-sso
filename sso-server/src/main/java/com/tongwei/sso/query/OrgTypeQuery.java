package com.tongwei.sso.query;

import org.apache.commons.lang.StringUtils;

import com.tongwei.auth.model.OrgType;
import com.tongwei.common.dao.BaseQuery;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * @author yangz
 * @date 2018年1月29日 下午12:43:19
 * @description 组织类型分页高级查询
 */
public class OrgTypeQuery extends BaseQuery<OrgType> {
    private static final long serialVersionUID = 1L;

    private String keyword;

    @Override
    public Example getExample() {
        this.example = new Example(entityClass);
        Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(keyword)) {
            criteria.andLike("name", "%"+keyword+"%");
            criteria.orEqualTo("code", keyword);
        }
        return example;
    }

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

}

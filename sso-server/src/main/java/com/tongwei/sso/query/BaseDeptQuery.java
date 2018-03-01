package com.tongwei.sso.query;

import org.apache.commons.lang.StringUtils;

import com.tongwei.common.dao.BaseQuery;
import com.tongwei.sso.model.BaseDept;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * @author yangz
 * @date 2018年1月29日 下午12:43:19
 * @description 基础部门分页高级查询
 */
public class BaseDeptQuery extends BaseQuery<BaseDept> {
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

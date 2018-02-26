package com.tongwei.common.dao;

import java.util.List;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author yangz
 * @date 2017年9月18日 下午7:45:06
 */
public interface CmMapper<T> extends Mapper<T>, MySqlMapper<T> {

    /**
     * 多表分页高级查询,需xml实现
     */
    List<T> queryMultiByPage(BaseQuery<T> query);
}

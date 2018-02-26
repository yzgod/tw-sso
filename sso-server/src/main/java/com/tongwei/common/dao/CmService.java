package com.tongwei.common.dao;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.entity.Example;

/**
 * @author yangz
 * @date 2017年3月1日 下午1:00:37
 * <p>
 * 介绍: 单表mapper配置通用Service接口
 */
public interface CmService<T, ID> {

    @Transactional
    int save(T t);

    @Transactional
    int saveUid(T t);

    @Transactional
    int update(T t);

    @Transactional
    int updateNotNull(T t);

    @Transactional
    int delete(ID id);

    @Transactional
    int deleteByProp(String property, String value);

    T get(ID id);

    boolean checkIfExist(ID id);

    boolean checkIfExist(String property, Object value);

    boolean checkIfExist(String[] properties, Object[] values);

    List<T> queryByPage(BaseQuery<T> baseQuery);

    List<T> findByProp(String property, Object value);

    List<T> findByPropNot(String property, Object value);

    List<T> findByPropLike(String property, Object value);

    @Transactional
    int updateByProp(ID id, String property, Object value);

    @Transactional
    int updateByProp(ID id, String[] properties, Object[] values);

    List<T> findByProp(String[] properties, Object[] values);

    List<T> findByPropAsc(String[] properties, Object[] values, String orderProperty);

    List<T> findByPropDesc(String[] properties, Object[] values, String orderProperty);

    List<T> getAll();

    List<T> query(T t);

    List<T> queryByExample(Example example);

    int getTotal(BaseQuery<T> baseQuery);

    int getTotal(Example example);

    List<T> findByPropAsc(String property, Object value, String orderProperty);

    List<T> findByPropDesc(String property, Object value, String orderProperty);

    int getTotal();

    T getOne(String property, Object value) throws IllegalArgumentException;

    int getCount(String property, Object value);

    int getCount(String[] properties, Object[] values);

    List<T> findPageRows(int offset, int limit, Example example);

    List<T> findPropIn(String property, List<String> vals);

    List<T> queryAsc(String orderProperty);

    List<T> queryDesc(String orderProperty);

    void insertList(List<T> list);

    List<T> queryMultiByPage(BaseQuery<T> query);

}

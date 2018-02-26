package com.tongwei.common.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;

import tk.mybatis.mapper.entity.Example;

/**
 * @author yangz
 * @date 2017年3月31日 下午1:09:28 
 * 介绍: 通用的query对象,包含了RowBounds分页,和count总记录数
 */
public class BaseQuery<T> implements Serializable {
    private static final long serialVersionUID = -4339443836640727123L;

    protected long total; // 总记录数

    protected int limit = 20; // 页大小

    protected int start; // 开始页数

    protected String sort; // 排序属性名

    protected String order; // desc asc排序

    protected Class<T> entityClass; // 实体类Class

    protected Example example; // example

    /**
     * 自动获取实体类 protected 供给子类调用
     */
    @SuppressWarnings("unchecked")
    protected BaseQuery() {
        Class<?> clz = getClass();
        Type type = clz.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            this.entityClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
        }
    }

    protected BaseQuery(int limit, int start) {
        this();
        this.start = start;
        if (limit != 0) {
            this.limit = limit;
        }
    }

    /**
     * 手动定义实体类
     * 
     * @param entityClass
     */
    public BaseQuery(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * <p>
     * 单表设计查询模型
     * <p>
     * 自定义查询条件覆写这个方法
     * <p>
     * 只适用于单表查询,多表需在mapper.xml中设计查询模型
     * 
     * @return 返回自定义的查询条件
     */
    public Example getExample() {
        // 配置Example
        this.example = new Example(entityClass);
        if (StringUtils.isNotBlank(sort)) {
            example.orderBy(sort);
        } else {
            return this.example;
        }
        if (StringUtils.isNotBlank(order)) {
            if (order.equalsIgnoreCase("desc")) {
                example.setOrderByClause(example.getOrderByClause() + " desc");
            }
            if (order.equalsIgnoreCase("asc")) {
                example.setOrderByClause(example.getOrderByClause() + " asc");
            }
        }
        return this.example;
    }

    public void setExample(Example example) {
        this.example = example;
    }

    /**
     * 从query对象中获取RowBounds
     * 
     * @return
     */
    public RowBounds getRowBounds() {
        return new RowBounds(start, limit);
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (limit > 100) {
            this.limit = 100;
            return;
        }
        if (limit <= 0) {
            this.limit = 20;
            return;
        }
        this.limit = limit;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public void setPage(Integer page) {
        this.start = page;
    }

    public void setRows(Integer rows) {
        setLimit(rows);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(start).append("_").append(limit);
        return sb.toString();
    }

}

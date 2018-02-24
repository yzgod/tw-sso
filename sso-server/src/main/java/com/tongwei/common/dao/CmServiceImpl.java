package com.tongwei.common.dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tongwei.common.util.ReflectUtil;

import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.mapperhelper.EntityHelper;


/**
 * @author yangz
 * @date 2017年3月1日 下午1:10:59
 * 介绍:service实现类
 */
public class CmServiceImpl<T,ID> implements CmService<T,ID> {
	/**
	 * 日志对象,子类直接调用记录日志信息
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	protected Class<T> entityClass;
	
	@SuppressWarnings("unchecked")
	public CmServiceImpl() {
		Class<?> clz = getClass();
		Type type = clz.getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) type;
			entityClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
		}
	}
	
	@Autowired
	private CmMapper<T> mapper;
	
	@Override
	public int save(T t){
		return mapper.insertSelective(t);
	}
	
	@Override
	public int saveUid(T t) {
		Set<EntityColumn> pkColumns = EntityHelper.getPKColumns(entityClass);
		pkColumns.forEach((column)->{
			String property = column.getProperty();
			if(String.class.equals(column.getJavaType())){
				ReflectUtil.setFieldValue(t, property, UUID.randomUUID().toString().replace("-","").toUpperCase());
			}
		});
		return mapper.insertSelective(t);
	}

	@Override
	public int update(T t) {
		return mapper.updateByPrimaryKey(t);
	}
	
	@Override
	public int updateNotNull(T t) {
		return mapper.updateByPrimaryKeySelective(t);
	}

	@Override
	public int delete(ID id) {
		return mapper.deleteByPrimaryKey(id);
	}
	
	@Override
	public T get(ID id) {
		return mapper.selectByPrimaryKey(id);
	}
	
	@Override
	public boolean checkIfExist(ID id) {
		return mapper.existsWithPrimaryKey(id);
	}

	@Override
	public List<T> getAll() {
		return mapper.selectAll();
	}
	
	@Override
	public List<T> query(T t) {
		return mapper.select(t);
	}

	@Override
	public List<T> queryByPage(BaseQuery<T> baseQuery) {
		Page<Object> page =null;
		try{
			page = PageHelper.startPage(baseQuery.getStart(), baseQuery.getLimit());
			return mapper.selectByExample(baseQuery.getExample());
		} finally {
			baseQuery.setTotal(page.getTotal());	//最终设置总数
		}
	}
	
	@Override
	public List<T> queryByExample(Example example) {
		return mapper.selectByExample(example);
	}

	@Override
	public int getTotal(Example example) {
		return mapper.selectCountByExample(example);
	}
	
	@Override
	public int getTotal(BaseQuery<T> baseQuery) {
		return mapper.selectCountByExample(baseQuery.getExample());
	}
	
	@Override
	public List<T> findByProp(String property, Object value) {
		Example example = new Example(entityClass);
		Criteria criteria = example.createCriteria();
		if(value==null){
			criteria.andIsNull(property);
		}else{
			criteria.andEqualTo(property, value);
		}
		return mapper.selectByExample(example);
	}
	
	@Override
	public List<T> findByPropAsc(String property, Object value,String orderProperty) {
		return findByPropOrderBy(property, value, orderProperty, " asc");
	}
	
	@Override
	public List<T> findByPropDesc(String property, Object value,String orderProperty) {
		return findByPropOrderBy(property, value, orderProperty, " desc");
	}
	
	@Override
	public List<T> findByPropNot(String property, Object value) {
		Example example = new Example(entityClass);
		Criteria criteria = example.createCriteria();
		criteria.andNotEqualTo(property, value);
		return mapper.selectByExample(example);
	}

	@Override
	public List<T> findByProp(String[] properties, Object[] values) {
		if(properties.length!=values.length){
			throw new RuntimeException("属性与值数量不相同");
		}
		Example example = new Example(entityClass);
		Criteria criteria = example.createCriteria();
		for (int i = 0; i < properties.length; i++) {
			criteria.andEqualTo(properties[i], values[i]);
		}
		return mapper.selectByExample(example);
	}

	@Override
	public int updateByProp(ID id, String property, Object value) {
		Set<EntityColumn> pkColumns = EntityHelper.getPKColumns(entityClass);
		String pk = "";
		for (EntityColumn entityColumn : pkColumns) {
			pk = entityColumn.getProperty();
		}
		T newInstance =null;
		try {
			newInstance = entityClass.newInstance();
			ReflectUtil.invokeSetter(newInstance, pk, id);
			ReflectUtil.invokeSetter(newInstance, property, value);
			return mapper.updateByPrimaryKeySelective(newInstance);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int updateByProp(ID id, String[] properties, Object[] values) {
		if(properties.length!=values.length){
			throw new RuntimeException("属性与值数量不相同");
		}
		Set<EntityColumn> pkColumns = EntityHelper.getPKColumns(entityClass);
		String pk = "";
		for (EntityColumn entityColumn : pkColumns) {
			pk = entityColumn.getProperty();
		}
		T newInstance =null;
		try {
			newInstance = entityClass.newInstance();
			ReflectUtil.invokeSetter(newInstance, pk, id);
			for (int i = 0; i < properties.length; i++) {
				ReflectUtil.invokeSetter(newInstance, properties[i], values[i]);
			}
			return mapper.updateByPrimaryKeySelective(newInstance);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public List<T> findByPropLike(String property, Object value) {
		Example example = new Example(entityClass);
		Criteria criteria = example.createCriteria();
		criteria.andLike(property, "%".concat(value.toString()).concat("%"));
		return mapper.selectByExample(example);
	}
	
	@Override
	public List<T> findByPropAsc(String[] properties, Object[] values,String orderProperty) {
		return findByPropOrderBy(properties,values,orderProperty," asc");
	}

	@Override
	public List<T> findByPropDesc(String[] properties, Object[] values,String orderProperty) {
		return findByPropOrderBy(properties,values,orderProperty," desc");
	}
	
	private List<T> findByPropOrderBy(String property, Object value,String orderProperty,String sort) {
		Example example = new Example(entityClass);
		example.orderBy(orderProperty);
		example.setOrderByClause(example.getOrderByClause().concat(sort));
		Criteria criteria = example.createCriteria();
		if(value==null){
			criteria.andIsNull(property);
		}else{
			criteria.andEqualTo(property, value);
		}
		return mapper.selectByExample(example);
	}
	
	private List<T> findByPropOrderBy(String[] properties, Object[] values,String orderProperty,String sort) {
		if(properties.length!=values.length){
			throw new RuntimeException("属性与值数量不相同");
		}
		Example example = new Example(entityClass);
		example.orderBy(orderProperty);
		example.setOrderByClause(example.getOrderByClause().concat(sort));
		Criteria criteria = example.createCriteria();
		for (int i = 0; i < properties.length; i++) {
			criteria.andEqualTo(properties[i], values[i]);
		}
		return mapper.selectByExample(example);
	}
	
	private List<T> findOrderBy(String orderProperty,String sort) {
		Example example = new Example(entityClass);
		example.orderBy(orderProperty);
		example.setOrderByClause(example.getOrderByClause().concat(sort));
		return mapper.selectByExample(example);
	}
	
	@Override
	public int getTotal() {
		return mapper.selectCountByExample(null);
	}

	@Override
	public T getOne(String property, Object value) throws IllegalArgumentException {
		Example example = new Example(entityClass);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo(property, value);
		List<T> list = mapper.selectByExample(example);
		if(list.isEmpty()){
			return null;
		}else if(list.size()==1){
			return list.get(0);
		}else{
			throw new IllegalArgumentException("查询结果包含多条数据!");
		}
	}
	
	@Override
	public int getCount(String property, Object value){
		Example example = new Example(entityClass);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo(property, value);
		return getTotal(example);
	}

	@Override
	public int getCount(String[] properties, Object[] values) {
		if(properties.length!=values.length){
			throw new RuntimeException("属性与值数量不相同");
		}
		Example example = new Example(entityClass);
		Criteria criteria = example.createCriteria();
		for (int i = 0; i < properties.length; i++) {
			criteria.andEqualTo(properties[i], values[i]);
		}
		return getTotal(example);
	}

	@Override
	public List<T> findPageRows(int offset, int limit, Example example) {
		return mapper.selectByExampleAndRowBounds(example, new RowBounds(offset,limit));
	}

	@Override
	public List<T> findPropIn(String property, List<String> vals) {
		Example example = new Example(entityClass);
		Criteria criteria = example.createCriteria();
		criteria.andIn(property, vals);
		return mapper.selectByExample(example);
	}

	@Override
	public List<T> queryMultiByPage(BaseQuery<T> query) {
		Page<Object> page =null;
		try{
			page = PageHelper.startPage(query.getStart(), query.getLimit());
			return mapper.queryMultiByPage(query);
		} finally {
			query.setTotal(page.getTotal());	//最终设置总数
		}
	}
	
	@Override
	public boolean checkIfExist(String property,Object value) {
		List<T> list = findByProp(property, value);
		if(list.size()==0){
			return false;
		}
		return true;
	}
	
	@Override
	public boolean checkIfExist(String[] properties,Object[] values) {
		List<T> list = findByProp(properties, values);
		if(list.size()==0){
			return false;
		}
		return true;
	}
	
	@Override
	public void insertList(List<T> list) {
		mapper.insertList(list);
	}

	@Override
	public List<T> queryAsc(String orderProperty) {
		return findOrderBy(orderProperty," asc");
	}

	@Override
	public List<T> queryDesc(String orderProperty) {
		return findOrderBy(orderProperty," desc");
	}

	@Override
	public int deleteByProp(String property, String value) {
		Example example = new Example(entityClass);
		Criteria criteria = example.createCriteria();
		if(value==null){
			criteria.andIsNull(property);
		}else{
			criteria.andEqualTo(property, value);
		}
		return mapper.deleteByExample(example);
	}

	
}

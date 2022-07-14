package com.hung.Ecommerce.Service;

import java.util.List;
import java.util.Map;

import com.hung.Ecommerce.Repository.SelectionOrder;

public interface AppService<T> {

	public T findById(int id, boolean fetchOrNot);
	public List<T> findAll(boolean fetchOrNot);
	public List<T> findByProperty(String propertyName, Object value, boolean trueIsAscending_falseIsDescending, boolean fetchOrNot);
	public List<T> findByProperties(Map<String, Object> conditions, Map<String, SelectionOrder> orders, boolean TrueIsAnd_FalseIsOr, boolean fetchOrNot) throws IllegalArgumentException;
	public void save(T theOne);
	public void reconnect(T player);
	public T update(T player);
	public int updateByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr, String targetField, Object newValue);
	public void delete(int id);
	public int deleteByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr);
}

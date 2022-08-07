package com.hung.Ecommerce.Repository;

import java.util.List;
import java.util.Map;

import com.hung.Ecommerce.DTO.SearchProductResult;
import com.hung.Ecommerce.Model.Product;
import com.hung.Ecommerce.Model.Product_SortTypes;

public interface ProductRepository extends CustomRepository<Product> {

	public List<Product> findManyByProperty(String propertyName, Object[] values, boolean trueIsAscending_falseIsDescending,
																boolean fetchOrNot);
	
	public SearchProductResult searchByName(String keyWords, int offset, int limit, Product_SortTypes sortType
																	, Map<String, String[]> filters);
	public SearchProductResult searchByCategory(String keyWords, int offset, int limit, Product_SortTypes sortType
																	, Map<String, String[]> filters);
}

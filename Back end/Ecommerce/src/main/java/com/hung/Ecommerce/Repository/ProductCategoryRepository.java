package com.hung.Ecommerce.Repository;

import java.util.List;

import com.hung.Ecommerce.Model.Product;
import com.hung.Ecommerce.Model.ProductCategory;

public interface ProductCategoryRepository extends CustomRepository<ProductCategory>{

	public List<ProductCategory> findManyByProperty(String propertyName, List<Object> values, boolean trueIsAscending_falseIsDescending,
			boolean fetchOrNot);
	
	public List<ProductCategory> searchByDepth(int depth, int offset, int limit);
	public List<String> getSuggestions(String keyWords, int offset, int limit);
}

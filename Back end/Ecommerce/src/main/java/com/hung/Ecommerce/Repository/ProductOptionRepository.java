package com.hung.Ecommerce.Repository;

import java.util.List;

import com.hung.Ecommerce.Model.Product;
import com.hung.Ecommerce.Model.ProductOption;

public interface ProductOptionRepository extends CustomRepository<ProductOption> {

	public List<ProductOption> findManyByProperty(String propertyName, List<Object> values, boolean trueIsAscending_falseIsDescending,
			boolean fetchOrNot);
}

package com.hung.Ecommerce.Service;

import java.util.List;

import com.hung.Ecommerce.Model.ProductOption;

public interface ProductOptionService extends AppService<ProductOption> {

	public List<ProductOption> findManyByProperty(String propertyName, List<Object> values, boolean trueIsAscending_falseIsDescending,
			boolean fetchOrNot);
}

package com.hung.Ecommerce.Repository;

import java.util.List;

import com.hung.Ecommerce.Model.Product;
import com.hung.Ecommerce.Model.ProductOrder;

public interface ProductOrderRepository extends CustomRepository<ProductOrder> {

	public List<ProductOrder> findManyByProperty(String propertyName, List<Object> values, boolean trueIsAscending_falseIsDescending,
			boolean fetchOrNot);
}

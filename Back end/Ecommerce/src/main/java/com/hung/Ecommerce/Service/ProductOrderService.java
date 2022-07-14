package com.hung.Ecommerce.Service;

import java.util.List;

import com.hung.Ecommerce.DTO.RequestToCancelOrder;
import com.hung.Ecommerce.DTO.RequestToPlaceOrder;
import com.hung.Ecommerce.Model.ProductOrder;

public interface ProductOrderService extends AppService<ProductOrder> {

	public ProductOrder addOrder(RequestToPlaceOrder request) throws Exception;
	public void cancelOrder(RequestToCancelOrder request) throws Exception;
	public List<ProductOrder> findManyByProperty(String propertyName, List<Object> values, boolean trueIsAscending_falseIsDescending,
			boolean fetchOrNot);
}

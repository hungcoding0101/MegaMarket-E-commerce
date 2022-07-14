package com.hung.Ecommerce.DTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonView;
import com.hung.Ecommerce.CustomExceptions.EntityChangedException;
import com.hung.Ecommerce.Model.Product;
import com.hung.Ecommerce.Model.ProductOrder;
import com.hung.Ecommerce.Util.JsonConverter.Views;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
public class PlaceOrderResult {
	
	@JsonView(value = {Views.JSONOrder_result.class})
	private Map<Integer,ProductOrder> successOrders;
	
	@JsonView(value = {Views.JSONOrder_result.class})
	private Map<Integer, EntityChangedException> failed;
	
	public PlaceOrderResult() {
		super();
		this.successOrders = new HashMap<>();
		this.failed = new HashMap<>();
	}
	
	
}

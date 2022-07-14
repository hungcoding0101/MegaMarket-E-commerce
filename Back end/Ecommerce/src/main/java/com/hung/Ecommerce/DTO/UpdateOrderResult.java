package com.hung.Ecommerce.DTO;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonView;
import com.hung.Ecommerce.CustomExceptions.EntityChangedException;
import com.hung.Ecommerce.Model.ProductOrder;
import com.hung.Ecommerce.Util.JsonConverter.Views;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UpdateOrderResult {

	@JsonView(value = {Views.JSONOrder_update_result.class})
		private List<String> success;
	
	@JsonView(value = {Views.JSONOrder_update_result.class})
		private Map<String, String> failed;
	
}

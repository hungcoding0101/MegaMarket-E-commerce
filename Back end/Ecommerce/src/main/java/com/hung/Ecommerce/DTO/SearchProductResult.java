package com.hung.Ecommerce.DTO;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonView;
import com.hung.Ecommerce.Model.Product;
import com.hung.Ecommerce.Util.JsonConverter.Views;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SearchProductResult {
	
	@JsonView(value = {Views.JSONProduct_search_result.class})
		private List<Product> products;	
	
	@JsonView(value = {Views.JSONProduct_search_result.class})
		private Long matchTotal;	
	
	@JsonView(value = {Views.JSONProduct_search_result.class})
		private Map<String, Long> sellers;

	@JsonView(value = {Views.JSONProduct_search_result.class})
		private Map<String, Long> brands;
}

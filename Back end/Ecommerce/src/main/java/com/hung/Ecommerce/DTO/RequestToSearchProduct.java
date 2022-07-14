package com.hung.Ecommerce.DTO;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hung.Ecommerce.Model.ProductOption;
import com.hung.Ecommerce.Model.Product_SortTypes;
import com.hung.Ecommerce.Util.JsonConverter.JsonToProductUpload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize(using = JsonToProductUpload.class)
public class RequestToSearchProduct {

	private String keyWords;
	private int offset;
	private int limit;
	private Product_SortTypes sortType;
	private Map<String, String> filters;
}

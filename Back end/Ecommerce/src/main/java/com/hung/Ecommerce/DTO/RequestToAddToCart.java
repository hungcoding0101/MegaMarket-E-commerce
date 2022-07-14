package com.hung.Ecommerce.DTO;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hung.Ecommerce.Model.ProductOption;
import com.hung.Ecommerce.Util.JsonConverter.JsonToAddToCart;
import com.hung.Ecommerce.Util.JsonConverter.JsonToProductUpload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize(using = JsonToAddToCart.class)
public class RequestToAddToCart  {
	
	@NotBlank
	private String productId;
	
	@NotNull
	@Min (value = 1, message = "{Validation.Minimum.Number}")
	private int quantity;
	
	private Set<Integer> optionsIds;
	
	@NotBlank
	private String customerName;
	
}

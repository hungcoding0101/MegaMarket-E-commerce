package com.hung.Ecommerce.DTO;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hung.Ecommerce.CustomAnnotation.ProperProductPrice;
import com.hung.Ecommerce.Util.JsonConverter.JsonToAddToCart;
import com.hung.Ecommerce.Util.JsonConverter.Views;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@JsonDeserialize(using = JsonToAddToCart.class)
public class RequestToPlaceOrder {


	@NotNull
		private Integer cartId;
	
	@NotBlank
		private String deliveryAddress;
	
	@NotBlank
		private String paymentMethod;
	
	@Min (value = 1, message = "{Validation.Minimum.Number}")
	@NotNull(message = "{Validation.Presence}")
		private int quantity;
	
	@NotBlank
		private String customerName;
}

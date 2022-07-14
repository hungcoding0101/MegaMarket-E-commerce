package com.hung.Ecommerce.DTO;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hung.Ecommerce.CustomAnnotation.ProperProductPrice;
import com.hung.Ecommerce.Model.ProductOption;
import com.hung.Ecommerce.Util.JsonConverter.JsonToProductUpload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize(using = JsonToProductUpload.class)
public class RequestToUploadProduct {


	@NotBlank(message = "{Validation.Format.ProductName}")
		private String name;	
	
	@NotEmpty(message = "{Validation.Presence.ProductCategory}")
		private Set<String> categories;
	
	@NotNull(message = "Empty seller name")
		private String sellerName;
		
	@NotNull(message = "{Validation.Presence.Price}")
	@ProperProductPrice
		private Double defaultUnitPrice;
	
	@NotNull(message = "{Validation.Presence.Price}")
	@ProperProductPrice
		private Double deliveryFeePerUnit;
	
	@NotNull(message = "{Validation.Presence.Price}")
	@ProperProductPrice
		private Double maxDeliveryFee;
	
	@NotNull(message = "You must provide total stock")
	@Min(0)
		private int totalStock;
	
	@NotNull(message = "{Validation.Presence.DeliveryTime}")
		private String estimatedDeliveryTime;
	
		private String unit;	
		private String brand;
		private Set<String> characteristics;
		private Set<ProductOption> productOptions;
		private Map<String, String> specificDetails;
		private String productDescription;
		private Set<String> paymentMethods;
}

package com.hung.Ecommerce.DTO;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonView;
import com.hung.Ecommerce.Model.Product;
import com.hung.Ecommerce.Util.JsonConverter.Views;
import com.hung.Ecommerce.Util.JsonConverter.Views.JSONProduct_search_result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UploadProductResult {


		private Product uploadedProduct;
	

		private Map<String, String> failedFileList;
}

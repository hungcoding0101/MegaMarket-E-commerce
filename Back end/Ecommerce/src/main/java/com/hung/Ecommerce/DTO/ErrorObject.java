package com.hung.Ecommerce.DTO;

import java.util.List;
import java.util.Map;

import com.hung.Ecommerce.Model.ProductOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorObject {
	private String error;
	private String message;
	private String path;
}

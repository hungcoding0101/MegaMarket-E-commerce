package com.hung.Ecommerce.DTO;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestToResetPasswords {

	@NotBlank
		private String code;
	
	@NotBlank
		private String newPasswords;
}

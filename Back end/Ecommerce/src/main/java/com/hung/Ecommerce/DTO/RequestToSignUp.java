package com.hung.Ecommerce.DTO;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.hung.Ecommerce.CustomAnnotation.UniqueEmail;
import com.hung.Ecommerce.CustomAnnotation.UniquePhoneNumber;
import com.hung.Ecommerce.CustomAnnotation.UniqueUserName;
import com.hung.Ecommerce.Model.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestToSignUp {

	@NotBlank(message = "{Validation.Format.UserName}")
		private String username;
	
	@Pattern(regexp = ".{8,}", message = "{Validation.Format.Password}")
	@NotBlank(message = "{Validation.Format.Password}")
	private String password;
	

	@Email(message = "{Validation.Format.Email}")
	@NotBlank(message = "{Validation.Format.Email}")
		private String email;
	

	@Pattern(regexp = "^0[\\d]{9}$", message = "{Validation.Format.PhoneNumber}")
	@NotBlank(message = "{Validation.Format.PhoneNumber}")
		private String phoneNumber;
	
	@NotBlank(message = "{Validation.Presence}")
		private String role;
}

package com.hung.Ecommerce.DTO;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.lang.Nullable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestToUpdateUserInfo {

//	@NotBlank(message = "{Validation.Format.UserName}")
		private String username;

	@Nullable
	@Pattern(regexp = ".{8,}", message = "{Validation.Format.Password}")
//	@NotBlank(message = "{Validation.Format.Password}")
		private String password;
	
	@NotBlank(message = "{Validation.Format.Password}")
		private String currentPasswords; 
	
		@Nullable
	@Email(message = "{Validation.Format.Email}")
//	@NotBlank(message = "{Validation.Format.Email}")
		private String email;
	
	@Nullable
	@Pattern(regexp = "^0[\\d]{9}$", message = "{Validation.Format.PhoneNumber}")
//	@NotBlank(message = "{Validation.Format.PhoneNumber}")
		private String phoneNumber;
}

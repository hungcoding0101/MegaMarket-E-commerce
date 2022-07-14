package com.hung.Ecommerce.CustomAnnotation;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.hung.Ecommerce.Model.User;
import com.hung.Ecommerce.Service.UserService;

@Configurable
public class UniquePhoneNumberImp implements  ConstraintValidator<UniquePhoneNumber, String>{

private UserService userService;

	
	public UniquePhoneNumberImp() {
	super();
}

	@Autowired
		public UniquePhoneNumberImp(UserService userService) {
			super();
			this.userService = userService;
		}

	@Override
	public void initialize(UniquePhoneNumber  constraintAnnotation) {
	
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		List<User> result = userService.findByProperty("phoneNumber", value, true, false);	
		return result.isEmpty();
	}
}

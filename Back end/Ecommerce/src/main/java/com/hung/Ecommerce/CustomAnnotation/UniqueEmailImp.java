package com.hung.Ecommerce.CustomAnnotation;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.hung.Ecommerce.Model.User;
import com.hung.Ecommerce.Service.UserService;

@Configurable
public class UniqueEmailImp implements  ConstraintValidator<UniqueEmail, String>{

	private UserService userService;
	
	
	public UniqueEmailImp() {
		super();
	}

	@Autowired
		public UniqueEmailImp(UserService userService) {
			super();
			this.userService = userService;
		}

	@Override
	public void initialize(UniqueEmail constraintAnnotation) {
	
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		List<User> result = userService.findByProperty("email", value, true, false);	
		return result.isEmpty();
	}
}

package com.hung.Ecommerce.CustomAnnotation;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.hung.Ecommerce.Model.User;
import com.hung.Ecommerce.Service.UserService;
import com.hung.Ecommerce.Service.UserServiceImp;

@Configurable(preConstruction = true)
public class UniqueUserNameImp implements  ConstraintValidator<UniqueUserName, String>{
	
	@Autowired
		private UserServiceImp userService;
	
	public UniqueUserNameImp() {
	}

//	@Autowired
//		public UniqueUserNameImp(UserService userService) {
//			this.userService = userService;
//		}



	@Override
	public void initialize(UniqueUserName constraintAnnotation) {
	
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		List<User> result = userService.findByProperty("username", value, true, false);	
		return result.isEmpty();
	}

}

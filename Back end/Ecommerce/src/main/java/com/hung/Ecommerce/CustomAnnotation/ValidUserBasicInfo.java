package com.hung.Ecommerce.CustomAnnotation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.hung.Ecommerce.Model.User;
import com.hung.Ecommerce.Service.UserService;

@Component
public class ValidUserBasicInfo implements Validator{

	@Autowired
		UserService userService;
	
	@Override
		public boolean supports(Class<?> clazz) {
			return User.class.isAssignableFrom(clazz);
		}

	@Override
		public void validate(Object target, Errors errors) {
			User user = (User) target;
			
			Map<String, Object> conditions = new HashMap<>();
			conditions.put("username", user.getUsername());
			conditions.put("email", user.getEmail());
			conditions.put("phoneNumber", user.getPhoneNumber());
			
			List<User> result = userService.findByProperties(conditions, null ,false, false);
			
			
			if( !result.isEmpty()) {
				
				for(User currentUser: result) {
					if(currentUser.getUsername().equals(user.getUsername())) {
						errors.rejectValue("username", "username", "duplicate username");
					}
					
					if(currentUser.getEmail().equals(user.getEmail())) {
						errors.rejectValue("email", "email", "duplicate email");
					}
					
					if(currentUser.getPhoneNumber().equals(user.getPhoneNumber())) {
						errors.rejectValue("phoneNumber", "phoneNumber", "duplicate phoneNumber");
					}
				}
				
			}
			
		}

}

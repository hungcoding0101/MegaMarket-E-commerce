package com.hung.Ecommerce.Controller;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.annotation.JsonView;
import com.hung.Ecommerce.CustomExceptions.GeneralException;
import com.hung.Ecommerce.DTO.RequestToAddToCart;
import com.hung.Ecommerce.Model.CartItem;
import com.hung.Ecommerce.Model.Customer;
import com.hung.Ecommerce.Service.CartItemService;
import com.hung.Ecommerce.Service.CustomerService;
import com.hung.Ecommerce.Util.JsonConverter.Views;

@RestController
@RequestMapping("/cart")
public class CartController {
	
	@Autowired
		private CartItemService cartItemService;
	
	@Autowired
		private CustomerService customerService;

	@PostMapping("/add")
	@JsonView(value = {Views.JSONCartItem_private.class})
		public ResponseEntity<CartItem> addToCart(OAuth2Authentication authentication,
																				@RequestBody (required = true) RequestToAddToCart request){
			
		CartItem result;
		
		try {
			request.setCustomerName(authentication.getName());
			result =cartItemService.addToCart(request); 
			return ResponseEntity.status(HttpStatus.CREATED).body(result);
		} catch (Exception e) {
			if(e instanceof ResponseStatusException){
				throw e;
			}
			
			if(e instanceof ConstraintViolationException) {
				e.printStackTrace();
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid value:");
			}
			
			else {
				e.printStackTrace();
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error occurred");}
		}
		
	}
	
	@DeleteMapping("/delete/{id}")
		public ResponseEntity<Integer> deleteCart(OAuth2Authentication authentication, @PathVariable(required = true) Integer id)
																		throws GeneralException{
						
			try {
				cartItemService.deleteCart(id, authentication.getName());
				return ResponseEntity.status(HttpStatus.OK).body(id);
			}catch (ResponseStatusException e) {
				throw e;
			}catch (Exception e) {
				e.printStackTrace();
				throw new GeneralException("Error occurred");
			}
			
	}
}

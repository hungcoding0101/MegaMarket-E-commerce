package com.hung.Ecommerce.Controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.annotation.JsonView;
import com.hung.Ecommerce.DTO.RequestToAddToCart;
import com.hung.Ecommerce.DTO.RequestToSignUp;
import com.hung.Ecommerce.DTO.RequestToUpdateAddresses;
import com.hung.Ecommerce.Model.CartItem;
import com.hung.Ecommerce.Model.Customer;
import com.hung.Ecommerce.Model.User;
import com.hung.Ecommerce.Service.CartItemService;
import com.hung.Ecommerce.Service.CustomerService;
import com.hung.Ecommerce.Util.JsonConverter.Views;
import com.nimbusds.oauth2.sdk.GeneralException;

import static java.lang.System.*;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/customer")
public class CustomerController {

	@Autowired
		CustomerService customerService;
	
	@GetMapping("/getAllInfo")
	@JsonView(value = {Views.JSONCustomer_private.class})
		public ResponseEntity<Customer> getCustomer(OAuth2Authentication authentication){
				List<Customer> result = customerService.findByProperty("username", authentication.getName(), false, true);
				if(!result.isEmpty()) {
						return ResponseEntity.status(HttpStatus.OK).body(result.get(0));
				}
				else {
					throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no user found");
				}
	}
	
	@PatchMapping("/updateAddresses")
		public ResponseEntity<String> updateAddresses(@RequestBody List<String> addresses,
						OAuth2Authentication authentication) throws GeneralException {
				try {
						customerService.updateAddresses(addresses, authentication.getName());
						return ResponseEntity.ok("");
				} catch (Exception e) {
					throw new GeneralException("");
				}
	}
}

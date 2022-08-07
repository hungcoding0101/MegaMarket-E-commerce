package com.hung.Ecommerce.Controller;

import javax.persistence.OptimisticLockException;
import static java.lang.System.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.aop.ThrowsAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.annotation.JsonView;
import com.hung.Ecommerce.CustomExceptions.EntityChangedException;
import com.hung.Ecommerce.CustomExceptions.GeneralException;
import com.hung.Ecommerce.DTO.PlaceOrderResult;
import com.hung.Ecommerce.DTO.RequestToAddToCart;
import com.hung.Ecommerce.DTO.RequestToCancelOrder;
import com.hung.Ecommerce.DTO.RequestToPlaceOrder;
import com.hung.Ecommerce.Model.ProductOrder;
import com.hung.Ecommerce.Service.ProductOrderService;
import com.hung.Ecommerce.Service.ProductService;
import com.hung.Ecommerce.Util.JsonConverter.Views;

@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired
		private ProductOrderService productOrderService;
	
	@Autowired
		private ProductService productService; 
	
	@PostMapping("/add")
	@JsonView(value = {Views.JSONOrder_result.class})
		public ResponseEntity<PlaceOrderResult> addOrders(OAuth2Authentication authentication,
				@RequestBody (required = true) RequestToPlaceOrder[] request) throws GeneralException{
		
		PlaceOrderResult result = new PlaceOrderResult();
		
		for(RequestToPlaceOrder rq: request) {
			try {
				rq.setCustomerName(authentication.getName());
				ProductOrder order = productOrderService.addOrder(rq);
				result.getSuccessOrders().put(rq.getCartId() ,order);
			} catch (EntityChangedException e) {
				result.getFailed().put(rq.getCartId(), e);
			}catch (OptimisticLockException e) {
				e.printStackTrace();
				result.getFailed().put(rq.getCartId(), new EntityChangedException("", "some information has changed"));
			}catch (Exception e) {
				e.printStackTrace();
				throw new GeneralException("");
			}
		} 
		out.println("HERE: RESULT: " + result.toString());
		return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(result);
	}
	
	@PatchMapping("/cancel")
		public void cancel(OAuth2Authentication authentication, @RequestParam String id) throws Exception{
				String username = authentication.getName() ;
				try {
						productOrderService.cancelOrder(new RequestToCancelOrder(id, username));
				} catch (ResponseStatusException e) {
					e.printStackTrace();
					throw e;
				}catch (Exception e) {
					e.printStackTrace();
					throw e;
				}
	}

}

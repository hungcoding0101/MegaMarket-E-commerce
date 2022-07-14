package com.hung.Ecommerce.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.hibernate.query.criteria.internal.expression.function.AggregationFunction.COUNT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.hung.Ecommerce.CustomExceptions.GeneralException;
import com.hung.Ecommerce.DTO.RequestToAddToCart;
import com.hung.Ecommerce.Model.CartItem;
import com.hung.Ecommerce.Model.Customer;
import com.hung.Ecommerce.Model.Product;
import com.hung.Ecommerce.Model.ProductOption;
import com.hung.Ecommerce.Model.ProductOptionStatus;
import com.hung.Ecommerce.Repository.CartItemRepository;
import com.hung.Ecommerce.Repository.CustomerRepository;
import com.hung.Ecommerce.Repository.ProductOptionRepository;
import com.hung.Ecommerce.Repository.ProductRepository;
import com.hung.Ecommerce.Repository.SelectionOrder;

@Service
@Transactional
public class CartItemServiceImp implements CartItemService{

	@Autowired
		private CartItemRepository cartItemRepository;
	
	@Autowired
		private CustomerService customerService;
	
	@Autowired
		private ProductService productService;
	
	@Autowired
		private ProductOptionService productOptionService;
	
	@Autowired
		private Validator validator;

	@Override
	public CartItem findById(int id, boolean fetchOrNot) {
		return cartItemRepository.findById(id, fetchOrNot);
	}

	@Override
	public List<CartItem> findAll(boolean fetchOrNot) {
		return cartItemRepository.findAll(fetchOrNot);
	}

	@Override
	public List<CartItem> findByProperty(String propertyName, Object value, boolean trueIsAscending_falseIsDescending,
			boolean fetchOrNot) {
		return cartItemRepository.findByProperty(propertyName, value, trueIsAscending_falseIsDescending, fetchOrNot);
	}

	@Override
	public List<CartItem> findByProperties(Map<String, Object> conditions, Map<String, SelectionOrder> orders, boolean TrueIsAnd_FalseIsOr, boolean fetchOrNot) throws IllegalArgumentException {
		return cartItemRepository.findByProperties(conditions, orders, TrueIsAnd_FalseIsOr, fetchOrNot);
	}

	@Override
	public void save(CartItem theOne) {
		 cartItemRepository.save(theOne);
	}

	@Override
	public void reconnect(CartItem cartItem) {
		cartItemRepository.reconnect(cartItem);
	}

	@Override
	public CartItem update(CartItem cartItem) {
		return cartItemRepository.update(cartItem);
	}

	@Override
	public int updateByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr, String targetField,
			Object newValue) {
		return cartItemRepository.updateByProperties(conditions, TrueIsAnd_FalseIsOr, targetField, newValue);
	}

	@Override
	public void delete(int id) {
		cartItemRepository.delete(id);
	}

	@Override
	public int deleteByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr) {
		return cartItemRepository.deleteByProperties(conditions, TrueIsAnd_FalseIsOr);
	}

	@Override
	public CartItem addToCart(RequestToAddToCart request) {

		Set<ConstraintViolation<RequestToAddToCart>> constraintViolations = validator.validate(request);
		if(!constraintViolations.isEmpty()) {
			for(ConstraintViolation<RequestToAddToCart> violation: constraintViolations) {
				String propertyPath = violation.getPropertyPath().toString();
				System.out.println("HERE: WRONG VALUE:" + propertyPath);
			}
				throw new ConstraintViolationException(constraintViolations);
		}
			
		List<Product> matchedProducts = productService.findByProperty("id", request.getProductId(), false, false);
		if(matchedProducts.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product not found");
		}		
		Product product = matchedProducts.get(0);
		
		List<Customer> matchedCustomer = customerService.findByProperty("username", request.getCustomerName(), false, false);
		if(matchedCustomer.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
		}		
		Customer customer = matchedCustomer.get(0);

		Set<ProductOption> matchedOptions  = new HashSet<>();
		Map<Integer, Long> choices_versions = new HashMap<>();
		Set<ProductOption> properOptions  = new HashSet<>();
		
		if(request.getOptionsIds() == null || request.getOptionsIds().isEmpty()) {
			if(!product.getProductOptions().isEmpty()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please choose an option");
			}
		}
			
		else {
			Set<ProductOption> thisProductOptions = product.getProductOptions();
					
			for(Integer id: request.getOptionsIds()) {
				ProductOption thisOption = thisProductOptions.stream().filter(option -> option.getId().compareTo(id) == 0)
						.findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid option: " + id));
				
				if(thisOption.getStatus() == ProductOptionStatus.DELETED) {
					throw new ResponseStatusException(HttpStatus.CONFLICT, "Option " + id + "is nolonger available");
				}				
				matchedOptions.add(thisOption);
			}
			
			if(matchedOptions.size() != product.getCharacteristics().size()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid options");
			}
			
			for(String characteristic: product.getCharacteristics()) {
				ProductOption optionWithThisCharacteristic = matchedOptions.stream().filter(option -> option.getCharacteristic()
						.equals(characteristic)).findFirst().
						orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid options"));
				properOptions.add(optionWithThisCharacteristic);
				choices_versions.put(optionWithThisCharacteristic.getId(), optionWithThisCharacteristic.getVersion());
			}
		}
		
		CartItem item = new CartItem();
		product.addCartItem(item);
		customer.addCartItem(item);
		item.setProductVersion(product.getVersion());
		item.setChoices(properOptions);
		item.setChoices_versions(choices_versions);
		item.setQuantity(request.getQuantity());
		item.setDeliveryFeePerUnit(product.getDeliveryFeePerUnit());
		item.setMaxDeliveryFee(product.getMaxDeliveryFee());
		item.setCreatedDate(LocalDateTime.now());
	
		try {
		cartItemRepository.save(item);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}

	@Override
	public void deleteCart(Integer id, String userName) {
		List<CartItem> matchedItem = cartItemRepository.findByProperty("id", id, false, false);
		if(matchedItem.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item not found");
		}
		
		CartItem item = matchedItem.get(0);
		
		if(!item.getCustomer().getUsername().equals(userName)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have authorization to delete this item");
		}
		
		try {
			item.getProduct().removeCartItem(item);
			cartItemRepository.delete(id);
		}catch (Exception e) {
			throw e;
		}		
	}
}

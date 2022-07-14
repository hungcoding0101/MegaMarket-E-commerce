package com.hung.Ecommerce.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.OptimisticLockException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.hung.Ecommerce.CustomExceptions.EntityChangedException;
import com.hung.Ecommerce.DTO.RequestToCancelOrder;
import com.hung.Ecommerce.DTO.RequestToPlaceOrder;
import com.hung.Ecommerce.Model.CartItem;
import com.hung.Ecommerce.Model.Customer;
import com.hung.Ecommerce.Model.Product;
import com.hung.Ecommerce.Model.ProductOption;
import com.hung.Ecommerce.Model.ProductOptionStatus;
import com.hung.Ecommerce.Model.ProductOrder;
import com.hung.Ecommerce.Model.ProductOrderStatus;
import com.hung.Ecommerce.Model.ProductStatus;
import com.hung.Ecommerce.Repository.ProductOrderRepository;
import com.hung.Ecommerce.Repository.ProductRepository;
import com.hung.Ecommerce.Repository.SelectionOrder;

@Service
@Transactional
public class ProductOrderServiceImp implements ProductOrderService {

	@Autowired
		private ProductOrderRepository ProductOrderRepository;
	
	@Autowired
		private ProductService productService;
	
	@Autowired
		private CartItemService cartItemService;
	
	@Autowired
		private ProductOptionService productOptionService;
	
	@Autowired
		private CustomerService customerService;

	@Override
	public ProductOrder findById(int id, boolean fetchOrNot) {
		return ProductOrderRepository.findById(id, fetchOrNot);
	}

	@Override
	public List<ProductOrder> findAll(boolean fetchOrNot) {
		return ProductOrderRepository.findAll(fetchOrNot);
	}

	@Override
	public List<ProductOrder> findByProperty(String propertyName, Object value, boolean trueIsAscending_falseIsDescending,
			boolean fetchOrNot) {
		return ProductOrderRepository.findByProperty(propertyName, value, trueIsAscending_falseIsDescending, fetchOrNot);
	}
	
	@Override
	public List<ProductOrder> findManyByProperty(String propertyName, List<Object> values,
			boolean trueIsAscending_falseIsDescending, boolean fetchOrNot) {
		return ProductOrderRepository.findManyByProperty(propertyName, values, trueIsAscending_falseIsDescending, fetchOrNot);
	}

	@Override
	public List<ProductOrder> findByProperties(Map<String, Object> conditions, Map<String, SelectionOrder> orders, boolean TrueIsAnd_FalseIsOr, boolean fetchOrNot) throws IllegalArgumentException {
		return ProductOrderRepository.findByProperties(conditions, orders, TrueIsAnd_FalseIsOr, fetchOrNot);
	}

	@Override
	public void save(ProductOrder theOne) {
		 ProductOrderRepository.save(theOne);
	}

	@Override
	public void reconnect(ProductOrder productOrder) {
		ProductOrderRepository.reconnect(productOrder);
	}

	@Override
	public ProductOrder update(ProductOrder productOrder) {
		return ProductOrderRepository.update(productOrder);
	}

	@Override
	public int updateByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr, String targetField,
			Object newValue) {
		return ProductOrderRepository.updateByProperties(conditions, TrueIsAnd_FalseIsOr, targetField, newValue);
	}

	@Override
	public void delete(int id) {
		ProductOrderRepository.delete(id);
	}

	@Override
	public int deleteByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr) {
		return ProductOrderRepository.deleteByProperties(conditions, TrueIsAnd_FalseIsOr);
	}

	@Override
	public ProductOrder addOrder(RequestToPlaceOrder request) throws Exception {

		try {
			
			List<CartItem> matchedCartItem = cartItemService.findByProperty("id", request.getCartId(), false, false);
			
			if(matchedCartItem.isEmpty()) {
				throw new EntityChangedException("Cart item", "not found");
			}		
			CartItem cartItem = matchedCartItem.get(0);
			
			Product product = cartItem.getProduct();
			if(product == null) {
				throw new ResponseStatusException(HttpStatus.CONFLICT, "Product not found");
			}
			
			List<Customer> matchedCustomer = customerService.findByProperty("username", request.getCustomerName(),
																		false, false);
			if(matchedCustomer.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.CONFLICT, "Customer not found");
			}	
			Customer customer = matchedCustomer.get(0);
			
			if(!customer.getDeliveryAddresses().contains(request.getDeliveryAddress())) {
					customer.getDeliveryAddresses().add(request.getDeliveryAddress());
			}
			
			if(product.getStatus() != ProductStatus.APPROVED) {
				throw new EntityChangedException("Product", "not available");
			}
			
			// In case product version has changed
			if(!product.getVersion().equals(cartItem.getProductVersion()) ) {	
				
					if(cartItem.getDeliveryFeePerUnit() * cartItem.getQuantity() < product.getMaxDeliveryFee()) {
							// If Delivery Fee Per Unit has increased
							if(cartItem.getDeliveryFeePerUnit()  < product.getDeliveryFeePerUnit()) {
								throw new EntityChangedException("Delivery Fee Per Unit", "has increased");
							}
					 }
					
					//If Max Delivery Fee has increased
					if(cartItem.getMaxDeliveryFee() < product.getMaxDeliveryFee()) {
						throw new EntityChangedException("Max Delivery Fee", "has increased");
					}
				
					// If payment method not available
					if(!product.getPaymentMethods().contains(request.getPaymentMethod())) {
						throw new EntityChangedException("Payment method", "not available");
					}		
			}		
			
			Set<ProductOption> choices = cartItem.getChoices();
			Set<ProductOption> properChoices = new HashSet<>();
			
			// In case the request not contains a choice			
			if(choices == null || choices.isEmpty()) {
				if(!product.getProductOptions().isEmpty()) {
					throw new EntityChangedException("", "Must contain an option");
				}
				
				if(cartItem.getUnitPrice() < product.getDefaultUnitPrice()) {
					throw new EntityChangedException("Product price", "has increased");
				}
				
				if(cartItem.getQuantity() > product.getTotalStock()) {
					throw new EntityChangedException("There remains only ", product.getTotalStock() 
							+ " items");
				}
			}
					
			// In case the request contains a choice
			else {
				for(String characteristic: product.getCharacteristics()) {
					ProductOption optionWithThisCharacteristic = choices.stream().filter(option -> option.getCharacteristic()
							.equals(characteristic)).findFirst().
							orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid options"));
					
					properChoices.add(optionWithThisCharacteristic);
				}
				
				for(ProductOption choice: properChoices) {
					if(choice.getStatus() == ProductOptionStatus.DELETED) {
						throw new EntityChangedException("Option", "is no longer available");
					}
					
					try {
						Long cart_optionVersion = cartItem.getChoices_versions().get(choice.getId());
						
						// In case the product option version has changed
						if(cart_optionVersion.compareTo(choice.getVersion()) != 0) {
							if(cartItem.getTotalPrice() < choice.getPriceDifference() + product.getDefaultUnitPrice()) {
								throw new EntityChangedException("Product price", "has increased");
							}
							
							if(cartItem.getQuantity() > choice.getStock()) {
								throw new EntityChangedException("There remains only ", choice.getStock() 
										+ " items for this option");
							}	
						}
					} catch (NullPointerException e) {
						throw new ResponseStatusException(HttpStatus.CONFLICT, "Option version not found");
					}
				}	
			}
					
			ProductOrder order = new ProductOrder();
			order.setPaymentMethod(request.getPaymentMethod());
			order.setStatus(ProductOrderStatus.ORDERED);
			order.setOrderDate(LocalDateTime.now());
			order.setChoices(properChoices);
			product.addProductOrder(order);
			order.setQuantity(request.getQuantity());
			order.setDeliveryAddress(request.getDeliveryAddress());
			order.setPhoneNumber(customer.getPhoneNumber());
			customer.addProductOrder(order);
			product.getSeller().addProductOrder(order);
			
			
			
			ProductOrderRepository.save(order);			 		

				product.setTotalStock(product.getTotalStock() - order.getQuantity(), order.getChoices());
			 		
			 Map<String, Object> condition = new HashMap<>();
			 condition.put("id", request.getCartId());
			 
			 CartItem item = cartItemService.findByProperty("id", request.getCartId(), false, false).get(0);
			 item.setChoices(null);
			 item.setChoices_versions(null);
			 
			 cartItemService.deleteByProperties(condition, false);
			 		
			 return order;
			} catch ( Exception e) {
				e.printStackTrace();
				throw e;
			}
		
	}

	@Override
	public void cancelOrder(RequestToCancelOrder request) throws Exception {
			List<ProductOrder> matchedOrder = ProductOrderRepository.findByProperty("id", request.getOrderId(), false, false);
			
			if(matchedOrder.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
			}
			
			ProductOrder order = matchedOrder.get(0);
			
			if(!order.getCustomer().getUsername().equals(request.getUsername())) {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to modify this order");
			}
			
			if(order.getStatus().equals(ProductOrderStatus.CANCELED)) {
				throw new ResponseStatusException(HttpStatus.CONFLICT, "You can't modify this order");
			}
			
			
			
			try {
				
				Product product = order.getProduct();
				
				ProductOrderStatus currentStatus = order.getStatus();
				
				order.setStatus(ProductOrderStatus.CANCELED);
				
				// if current status is changed from "DELIVERED" to "CANCELED", decrease the product's sales and increase it's stock
				if(currentStatus.equals(ProductOrderStatus.DELIVERED)){
						order.getCustomer().removePurchasedProduct(product);
						order.setDeliveryDate(null);
						
					int salesToUpdate = -order.getQuantity();
							
					product.setTotalSales(product.getTotalSales() + salesToUpdate, order.getChoices());
				}
				
				product.setTotalStock(product.getTotalStock() + order.getQuantity(), order.getChoices());
				
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}	
	}


}

package com.hung.Ecommerce.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.hung.Ecommerce.DTO.RequestToSignUp;
import com.hung.Ecommerce.DTO.RequestToUpdateOrder;
import com.hung.Ecommerce.DTO.RequestToUploadProduct;
import com.hung.Ecommerce.DTO.UpdateOrderResult;
import com.hung.Ecommerce.DTO.UploadProductResult;
import com.hung.Ecommerce.Model.Customer;
import com.hung.Ecommerce.Model.Notification;
import com.hung.Ecommerce.Model.NotificationStatus;
import com.hung.Ecommerce.Model.Product;
import com.hung.Ecommerce.Model.ProductCategory;
import com.hung.Ecommerce.Model.ProductOption;
import com.hung.Ecommerce.Model.ProductOrder;
import com.hung.Ecommerce.Model.ProductOrderStatus;
import com.hung.Ecommerce.Model.ProductStatus;
import com.hung.Ecommerce.Model.Seller;
import com.hung.Ecommerce.Model.StaticResourceMapper;
import com.hung.Ecommerce.Repository.SelectionOrder;
import com.hung.Ecommerce.Repository.SellerRepository;

@Service
@Transactional
public class SellerServiceImp implements SellerService{

	@Autowired
	private SellerRepository sellerRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
		private ProductService productService;
	
	@Autowired
	private ProductCategoryService productCategoryService;
	
	@Autowired
		private ProductOptionService productOptionService;
	
	@Autowired
		private ProductOrderService orderService;
	
	@Autowired
		private NotificationService notificationService;
	
	@Autowired
		private Validator validator;
	
	@Value("${external_resource.path.image}")
		private String imageSavingPath;
	
	@Autowired
		SavingFileAssistant fileSaver;
	

	@Override
	public Seller findById(int id, boolean fetchOrNot) {
		return sellerRepository.findById(id, fetchOrNot);
	}


	@Override
	public List<Seller> findAll(boolean fetchOrNot) {
		return sellerRepository.findAll(fetchOrNot);
	}

	@Override
	public List<Seller> findByProperty(String propertyName, Object value, boolean trueIsAscending_falseIsDescending,
			boolean fetchOrNot) {
		return sellerRepository.findByProperty(propertyName, value, trueIsAscending_falseIsDescending, fetchOrNot);
	}

	@Override
	public List<Seller> findByProperties(Map<String, Object> conditions, Map<String, SelectionOrder> orders, boolean TrueIsAnd_FalseIsOr, boolean fetchOrNot) throws IllegalArgumentException {
		return sellerRepository.findByProperties(conditions, orders, TrueIsAnd_FalseIsOr, fetchOrNot);
	}

	@Override
	public void save(Seller theOne) {
		 sellerRepository.save(theOne);
	}

	@Override
	public void reconnect(Seller seller) {
		sellerRepository.reconnect(seller);
	}

	@Override
	public Seller update(Seller seller) {
		return sellerRepository.update(seller);
	}

	@Override
	public int updateByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr, String targetField,
			Object newValue) {
		return sellerRepository.updateByProperties(conditions, TrueIsAnd_FalseIsOr, targetField, newValue);
	}

	@Override
	public void delete(int id) {
		sellerRepository.delete(id);
	}

	@Override
	public int deleteByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr) {
		return sellerRepository.deleteByProperties(conditions, TrueIsAnd_FalseIsOr);
	}
	
	@Override
	public void signUp(RequestToSignUp request) throws Exception {
		userService.signUp(request);
	}
	
	@Override
	public void logOut(String accessToken, String refreshToken) {
		userService.logOut( accessToken, refreshToken);
	}

	@Override
	public UploadProductResult uploadProduct(RequestToUploadProduct request, MultipartFile avatarImage,
			MultipartFile[] otherImages, MultipartFile[] optionImages) throws NoSuchAlgorithmException, ConstraintViolationException {

		Set<ConstraintViolation<RequestToUploadProduct>> constraintViolations = validator.validate(request);
		if(!constraintViolations.isEmpty()) {
			for(ConstraintViolation<RequestToUploadProduct> violation: constraintViolations) {
				String propertyPath = violation.getPropertyPath().toString();
				System.out.println("HERE: WRONG VALUE:" + propertyPath);
			}
				throw new ConstraintViolationException(constraintViolations);
		}
		
					
		// Find seller from seller name	
			Seller thisSeller = null;
			List<Seller> foundSellers =  sellerRepository.findByProperty("username", request.getSellerName(), false, false);	
			
			if(foundSellers.isEmpty()) {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seller not found");
			}
			
			thisSeller = foundSellers.get(0);
			

		// Find product categories based on names	
			List<ProductCategory> targetedCatefories = productCategoryService.findManyByProperty("title", new ArrayList<Object>(request.getCategories()), false, false);		
			if(targetedCatefories.isEmpty()) {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category not found");
			}
			
			Set<String> characteristics = request.getCharacteristics();
			Set<ProductOption> options = request.getProductOptions();
			
			for(String characteristic: characteristics) {
					List<ProductOption> optionsWithThisCharacteristic = options.stream().filter(option -> option.getCharacteristic()
																												.equals(characteristic)).collect(Collectors.toList());
						
					Integer total = optionsWithThisCharacteristic.stream().map(option -> option.getStock()).reduce(Integer::sum)
						.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Options do not match characteristics"));
						
					if(total.intValue() != request.getTotalStock()) {
						throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Options stock does not match total stock");
					}
			}
			
			Product newProduct = new Product();
			System.out.println("HERE: REQUEST: " + request);
			newProduct.setName(request.getName());
			newProduct.setDefaultUnitPrice(request.getDefaultUnitPrice());
			newProduct.setTotalStock(request.getTotalStock());
			newProduct.setDeliveryFeePerUnit(request.getDeliveryFeePerUnit());
			newProduct.setMaxDeliveryFee(request.getMaxDeliveryFee());
			newProduct.setBrand(request.getBrand());
			newProduct.setUnit(request.getUnit());
			newProduct.setEstimatedDeliveryTime(request.getEstimatedDeliveryTime());
			newProduct.setLastModificationTime(LocalDateTime.now());
			newProduct.setStatus(ProductStatus.APPROVED);
			newProduct.setProductDescription(request.getProductDescription());
			newProduct.setPaymentMethods(new HashSet<>(request.getPaymentMethods()));
			System.out.println("HERE: NEW PRODUCT: " + newProduct);
			
			thisSeller.addProduct(newProduct);
			
			newProduct.addCategories(new HashSet<>(targetedCatefories));
			
		if(!(request.getCharacteristics().isEmpty() || options.isEmpty())) {
			// add productOptions to this product		
						newProduct.addProductOptions(options);

			// add characteristics to this product
						newProduct.setCharacteristics(characteristics);
		}
			
		// add specific details to this product
			Map<String, String> specificDetails = request.getSpecificDetails();
			newProduct.setSpecificDetails(specificDetails);
		
			
			List<StaticResourceMapper> avatarResult = new LinkedList<StaticResourceMapper>();
			List<StaticResourceMapper> otherImagesResult = new LinkedList<StaticResourceMapper>();
			List<StaticResourceMapper> optionImagesResult = new LinkedList<StaticResourceMapper>();
			String imagePath = "/StaticResources/Images/Products/";
			
					try {
							if(avatarImage != null) {
									avatarResult = fileSaver.saveFile(new LinkedList<>(List.of(avatarImage)), imageSavingPath + "Products\\", "image");
									if(!avatarResult.isEmpty()) {
										newProduct.setAvatarImageURL(imagePath+avatarResult.get(0).getFileKey());
									}
							}
							
							if(optionImages != null && optionImages.length > 0) {
									for(ProductOption option: newProduct.getProductOptions()) {
											for(MultipartFile image: optionImages) {
													if(image.getOriginalFilename().equals(option.getImageUrl())) {
															List<StaticResourceMapper> mapper = fileSaver.saveFile(new LinkedList<>(List.of(image)),
																															imageSavingPath + "Products\\", "image");
															option.setImageUrl(imagePath+mapper.get(0).getFileKey());
													}
											}
									}
							}
						
							
							if(otherImages != null && otherImages.length > 0) {
									otherImagesResult = fileSaver.saveFile(new LinkedList<>(List.of(otherImages)), imageSavingPath + "Products\\", "image");
									if(!otherImagesResult.isEmpty()) {
											Stream<StaticResourceMapper> resultStream = otherImagesResult.stream();
											List<String> imageKeys = resultStream.map(mapper -> imagePath+mapper.getFileKey()).collect(Collectors.toList());
											newProduct.setOtherImagesURLS(new HashSet<>(imageKeys));
									}
							}
						} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
			productService.save(newProduct);
			
//			for(ProductOption option: options) {
//				productOptionService.save(option);
//			}			
			
			UploadProductResult result = new UploadProductResult(newProduct, fileSaver.getFailedList());
			
			return result;
	}
	
//	@Override
//	public UploadProductResult uploadProduct(RequestToUploadProduct request) throws NoSuchAlgorithmException, ConstraintViolationException {
//
//			Product newProduct = new Product();
//			System.out.println("HERE: REQUEST: " + request);
//			newProduct.setName(request.getName());
//			newProduct.setDefaultUnitPrice(request.getDefaultUnitPrice());
//			newProduct.setDeliveryFeePerUnit(request.getDeliveryFeePerUnit());
//			newProduct.setMaxDeliveryFee(request.getMaxDeliveryFee());
//			newProduct.setBrand(request.getBrand());
//			newProduct.setUnit(request.getUnit());
//			newProduct.setEstimatedDeliveryTime(request.getEstimatedDeliveryTime());
//			newProduct.setLastModificationTime(LocalDateTime.now());
//			newProduct.setStatus(ProductStatus.APPROVED);
//			newProduct.setProductDescription(request.getProductDescription());
//			newProduct.setPaymentMethods(new HashSet<>(request.getPaymentMethods()));
//			System.out.println("HERE: NEW PRODUCT: " + newProduct);
//			
//		// Find seller from seller name	
//			Seller thisSeller = null;
//			List<Seller> foundSellers =  sellerRepository.findByProperty("username", request.getSellerName(), false, false);	
//			if(!foundSellers.isEmpty()) {thisSeller = foundSellers.get(0);}
//			thisSeller.addProduct(newProduct);
//
//		// Find product categories based on names
//			List<ProductCategory> targetedCatefories = productCategoryService.findManyByProperty("title", new ArrayList<Object>(request.getCategories()), false, false);		
//					if(!targetedCatefories.isEmpty()) {
//							newProduct.addCategories(targetedCatefories);
//					}
//			
//			
//			if(request.getProductOptions().isEmpty()) {
//					if(request.getTotalStock() == 0) {
//						throw new ConstraintViolationException("Neither total stock nor options exists", null);
//					}
//					
//					else {
//						newProduct.setTotalStock(request.getTotalStock());
//					}
//			}
//			
//			else {
//				for(ProductOption option: request.getProductOptions()) {
//						newProduct.setTotalStock(newProduct.getTotalStock() + option.getStock());
//				}
//			}
//			
//		// add productOptions to this product
//			List<ProductOption> options = request.getProductOptions();		
//			newProduct.addProductOptions(options);
//				
//		
//		// add characteristics to this product
//			newProduct.setCharacteristics(new HashSet<>(request.getCharacteristics()));
//			
//			
//		// add specific details to this product
//			Map<String, String> specificDetails = request.getSpecificDetails();
//			newProduct.setSpecificDetails(specificDetails);
//		
//			
//			
//		// Now validate the new product using javax validator	
//			Set<ConstraintViolation<Product>> constraintViolations = validator.validate(newProduct);
//			if(!constraintViolations.isEmpty()) {
//				for(ConstraintViolation<Product> violation: constraintViolations) {
//					String propertyPath = violation.getPropertyPath().toString();
//					System.out.println("HERE: WRONG VALUE:" + propertyPath);
//				}
//					throw new ConstraintViolationException(constraintViolations);
//				}
//			
//			
//					
//			productService.save(newProduct);
//			
//		// If nothing wrong, save this product and created ProductOptions;
//			for(ProductOption option: options) {
//				productOptionService.save(option);
//			}
//			
//			
//			UploadProductResult result = new UploadProductResult(newProduct, fileSaver.getFailedList());
//			
//			return result;
//	}


	@Override
	public UpdateOrderResult updateOrder(RequestToUpdateOrder[] requests, OAuth2Authentication authentication) throws Exception{
			List<String> success = new LinkedList<String>();
			Map<String, String> failed = new HashMap<>();
						
				for(RequestToUpdateOrder request: requests) {
					try {
						List<ProductOrder> matchedOrder = orderService.findByProperty("id", request.getOrderId(), false, false);
						
						if(matchedOrder.isEmpty()) {
								failed.put(request.getOrderId(), "Order not found");
								continue;
						}
						
						ProductOrder order = matchedOrder.get(0);
						
						if(!order.getSeller().getUsername().equals(authentication.getName())) {
								failed.put(request.getOrderId(), "Your are not allowed to modify this order");
								continue;
						}
						
						if(order.getStatus().equals(ProductOrderStatus.CANCELED)) {
								failed.put(request.getOrderId(), "Can not modify this order because it was cancelled");
								continue;
						}
						
						ProductOrderStatus status = null;
						
						try {
							status = ProductOrderStatus.valueOf(request.getOrderStatus());
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						ProductOrderStatus currentStatus = order.getStatus();
						
						if(status == null || status.equals(ProductOrderStatus.CANCELED) || status.equals(currentStatus)) {
							failed.put(request.getOrderId(), "Invalid order status");
							continue;
						}
						
						order.setStatus(ProductOrderStatus.valueOf(request.getOrderStatus()));
						
				// Now check whether we need to update sales of the product and delivery date of the order	
						int quantityToUpdate = 0;
						Product product = order.getProduct();
						
				// if new status is "DELIVERED", increase the sales
						if(status.equals(ProductOrderStatus.DELIVERED)) {
								quantityToUpdate = order.getQuantity();
								order.getCustomer().addPurchasedProduct(product);
								order.setDeliveryDate(LocalDateTime.now());
						}
						
						// if current status is changed from "DELIVERED" to others, decrease the sales
						if(currentStatus.equals(ProductOrderStatus.DELIVERED)){
								quantityToUpdate = -order.getQuantity();
								order.getCustomer().removePurchasedProduct(product);
								order.setDeliveryDate(null);
						}
		
						product.setTotalSales(product.getTotalSales() + quantityToUpdate, order.getChoices());
						
						Notification notification = new Notification();
						notification.setSubject("Status of order " + order.getId() + " has been updated: " + order.getStatus().toString());
						notification.setDateSent(LocalDateTime.now());
						notification.setStatus(NotificationStatus.NEW);
						
						Customer customer = order.getCustomer();
						customer.addNotification(notification);
						
						notificationService.save(notification);
						
						success.add(request.getOrderId());

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				return new UpdateOrderResult(success, failed);
	}
	
}

package com.hung.Ecommerce.Controller;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.System.*;
import javax.mail.Multipart;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.websocket.server.PathParam;

import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hung.Ecommerce.CustomExceptions.GeneralException;
import com.hung.Ecommerce.DTO.MultipartHolder;
import com.hung.Ecommerce.DTO.RequestToPlaceOrder;
import com.hung.Ecommerce.DTO.RequestToSignUp;
import com.hung.Ecommerce.DTO.RequestToUpdateOrder;
import com.hung.Ecommerce.DTO.RequestToUploadProduct;
import com.hung.Ecommerce.DTO.UpdateOrderResult;
import com.hung.Ecommerce.DTO.UploadProductResult;
import com.hung.Ecommerce.Model.Customer;
import com.hung.Ecommerce.Model.Product;
import com.hung.Ecommerce.Model.ProductStatus;
import com.hung.Ecommerce.Model.Seller;
import com.hung.Ecommerce.Service.CustomerService;
import com.hung.Ecommerce.Service.ProductOrderService;
import com.hung.Ecommerce.Service.ProductService;
import com.hung.Ecommerce.Service.SellerService;
import com.hung.Ecommerce.Util.JsonConverter.Views;

@RestController
@RequestMapping("/seller")
public class SellerController {

	@Autowired
		private SellerService sellerService;
	
	@Autowired
		private CustomerService customerService;
	
	@Autowired
		private ProductService productService;
	
	@Autowired
		private ProductOrderService orderService;
	
	@Value("${external_resource.path.image}")
	private String imageSavingPath;
	
	
	@PostMapping("/signup")
		public ResponseEntity<String> SignUp(@RequestBody RequestToSignUp request, HttpServletRequest httpRequest,
										HttpServletResponse httpResponse) throws Exception {
		
			try {
				sellerService.signUp(request);
				return ResponseEntity.status(HttpStatus.CREATED).build();
				
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
	}
	
	@GetMapping("/getAllInfo")
	@JsonView(value = {Views.JSONSeller_private.class})
		public ResponseEntity<Seller> getCustomer(OAuth2Authentication authentication){
				List<Seller> result = sellerService.findByProperty("username", authentication.getName(), false, true);
				if(!result.isEmpty()) {
						return ResponseEntity.status(HttpStatus.OK).body(result.get(0));
				}
				else {
					throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no user found");
				}
	}
	
	@GetMapping("/getOwnedProducts")
	@JsonView(value = {Views.JSONProduct_private.class})
		public ResponseEntity<List<Product>> getOwnedProducts(OAuth2Authentication authentication) throws GeneralException{
		List<Product> matchedProducts;
		List<Seller> matchedSeller = sellerService.findByProperty("username", authentication.getName(), false, true);
		
		if(matchedSeller.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no user found");
		}
		
		Seller seller  = matchedSeller.get(0);
		
		try {
			 matchedProducts = productService.findByProperty("seller", seller, false, true);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GeneralException("");
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(matchedProducts);
	}
	
	
	@PostMapping ("/upload/product")
		public ResponseEntity<UploadProductResult> uploadProduct(
				OAuth2Authentication authentication,
				// Have to use @RequestParam to parse JSON String from Multipart request
				// Used a custom converter to convert JSON string to this RequestToUploadProduct
				@RequestParam (required = true) RequestToUploadProduct product
				,@RequestPart (required = false) MultipartFile avatarImage
				, @RequestPart (required = false)MultipartFile[] otherImages
				, @RequestPart (required = false)MultipartFile[] optionImages) throws NoSuchAlgorithmException {

				
//				MultipartHolder avatarHolder = null;
//				List<MultipartHolder> otherHolder = new ArrayList<MultipartHolder>();
//				List<MultipartHolder> optionHolder = new ArrayList<MultipartHolder>();
//				
//				if(avatarImage != null) {
//					try {
//						avatarHolder = new MultipartHolder(avatarImage.getBytes(), avatarImage.getOriginalFilename(),
//								avatarImage.getContentType());
//					} catch (IOException e1) {
//						e1.printStackTrace();
//					}
//				}
//				
//				
//				if(otherImages != null) {
//					for(MultipartFile file: otherImages) {
//						MultipartHolder holder;
//						try {
//							holder = new MultipartHolder(file.getBytes(), file.getOriginalFilename(), file.getContentType());
//							otherHolder.add(holder);
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//					}
//				}
//				
//				if(optionImages != null) {
//					for(MultipartFile file: optionImages) {
//						MultipartHolder holder;
//						try {
//							holder = new MultipartHolder(file.getBytes(), file.getOriginalFilename(), file.getContentType());
//							optionHolder.add(holder);
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//					}
//				}
		
				UploadProductResult result;
				try {
					product.setSellerName(authentication.getName());
					result = sellerService.uploadProduct(product, avatarImage
							,otherImages
							, optionImages);
//					productService.addImagesToProduct(result.getUploadedProduct().getId(), avatarHolder, otherHolder, optionHolder);
					out.println("HERE: UPDATE DONE!");
				}catch(ConstraintViolationException e) {
					Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
					StringBuilder builder = new StringBuilder();
					for(ConstraintViolation<?> violation: violations) {
						builder.append(violation.getPropertyPath() + ", ");
					}
					
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid inputs: " + builder, e);
				}
			
				out.println("HERE: uploaded product: " + result);
				return ResponseEntity.status(HttpStatus.CREATED).body(result);
	}
	
	@PatchMapping ("/update/orders")
	@JsonView(value = {Views.JSONOrder_update_result.class})
		public ResponseEntity<UpdateOrderResult> updateOders(OAuth2Authentication authentication,
				@RequestBody (required = true) RequestToUpdateOrder[] requests) throws Exception{
			
			try {
				UpdateOrderResult result = sellerService.updateOrder(requests, authentication);
				return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(result);
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
	}
	
}

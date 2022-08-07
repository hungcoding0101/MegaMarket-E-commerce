package com.hung.Ecommerce.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hung.Ecommerce.CustomExceptions.GeneralException;
import com.hung.Ecommerce.DTO.SearchProductResult;
import com.hung.Ecommerce.Model.Product;
import com.hung.Ecommerce.Model.ProductCategory;
import com.hung.Ecommerce.Model.ProductStatus;
import com.hung.Ecommerce.Model.Product_SortTypes;
import com.hung.Ecommerce.Service.CustomerService;
import com.hung.Ecommerce.Service.KeyWordsService;
import com.hung.Ecommerce.Service.ProductCategoryService;
import com.hung.Ecommerce.Service.ProductService;
import com.hung.Ecommerce.Service.SellerService;
import com.hung.Ecommerce.Util.Customtree.Tree;
import com.hung.Ecommerce.Util.Customtree.TreeNode;
import com.hung.Ecommerce.Util.JsonConverter.Views;

import static java.lang.System.*
;
@RestController
@RequestMapping("/products")
public class ProductController {

	@Autowired
			private ProductCategoryService productCategoryService;
	
	@Autowired
		private ProductService productService;
	
	@Autowired
		private CustomerService customerService;
	
	@Autowired
		private SellerService sellerService;
	
	@Autowired
		private KeyWordsService keyWordsService;
	


	@GetMapping("/categories/tree")
	@ResponseBody
		public String getCategoryTree() throws JsonProcessingException{
		out.println("HERE: ROOT GOT CALLED: "); 
			ObjectMapper mapper = new ObjectMapper();
			 ProductCategory[] categories =  {productCategoryService.getProductCategoryTree()};
			 out.println("HERE: ROOT: " + categories[0]); 
			return mapper.writerWithView(Views.JSONProductCategory_public.class).writeValueAsString(categories);
	}
	
	@GetMapping("/categories/all")
	@JsonView(value = {Views.JSONProductCategory_public.class})
	@ResponseBody
		public ResponseEntity<List<ProductCategory>> getAllCategories() throws JsonProcessingException{
		List<ProductCategory> categories = null;
			 try {
				categories = productCategoryService.findAll(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			 if(categories == null) {
				 throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Some error occurred");
			 }
			 
			return ResponseEntity.status(HttpStatus.OK).body(categories);
	}
	
	@GetMapping("/categories")
	@JsonView(value = {Views.JSONProductCategory_public.class})
	@ResponseBody
		public ResponseEntity<List<ProductCategory>> searchByDepth(@RequestParam(required = false) Integer depth,
				@RequestParam(required = false) Integer offset,
				@RequestParam(required = false) Integer limit) throws JsonProcessingException, GeneralException{
		
			if(depth == null || offset == null || limit == null) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "missing parameters");
			}
		
			List<ProductCategory> categories = null;
				 try {
					categories = productCategoryService.searchByDepth(depth, offset, limit);
				} catch (Exception e) {
					e.printStackTrace();
					throw new GeneralException("");
				}
			 
			return ResponseEntity.status(HttpStatus.OK).body(categories);
	}
	
	@GetMapping("/suggestions")
	@ResponseBody
		public ResponseEntity<List<String>> getSuggestions(@RequestParam String keyWords,
				@RequestParam Integer offset, @RequestParam Integer limit	) throws GeneralException{
		
			try {
				List<String> result = keyWordsService.search(keyWords, offset, limit);
					return ResponseEntity.status(HttpStatus.OK).body(result);
			} catch (Exception e) {
				e.printStackTrace();
				throw new GeneralException("");
			}		
	}
	
	@GetMapping("/{id}")
	@JsonView(value = {Views.JSONProduct_public.class})
		public ResponseEntity<Product> getProduct(@PathVariable String  id) throws GeneralException{
		List<Product> matchedProduct = new ArrayList<>();
		try {
			 matchedProduct = productService.findByProperty("id", id, false, true);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GeneralException("");
		}
		
		if(matchedProduct.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "products not found");
		}
		
		Product product = matchedProduct.get(0);
		
		if(!product.getStatus().equals(ProductStatus.APPROVED)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "product not available");
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(product);	
	}
	
	@PostMapping("/list")
	@JsonView(value = {Views.JSONProduct_public.class})
		public ResponseEntity<List<Product>> getProducts(@RequestBody Object[]  ids) throws GeneralException{		
			
			List<Product> matchedProducts = new LinkedList<>();
			try {
				 matchedProducts = productService.findManyByProperty("id", ids, false, true);
			} catch (Exception e) {
				e.printStackTrace();
				throw new GeneralException("");
			}
			
			if(matchedProducts.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "products not found");
			}
			
			for(int i = 0; i < matchedProducts.size(); i ++) {
				if(!matchedProducts.get(i).getStatus().equals(ProductStatus.APPROVED)) {
						matchedProducts.remove(i);
				}
			}
			
			return ResponseEntity.status(HttpStatus.OK).body(matchedProducts);
	
	}
	
	@GetMapping("/all")
	@JsonView(value = {Views.JSONProduct_public.class})
		public ResponseEntity<List<Product>> getProducts() throws Exception{
		
			try {
				List<Product>	foundProducts = productService.findByProperty("status", ProductStatus.APPROVED, false, true);
				
				out.println("HERE: ALL PRODUCTS:" + foundProducts.stream().map(p -> p.getId()).collect(Collectors.toList()));
				return ResponseEntity.status(HttpStatus.OK).body(foundProducts);
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
	}
	
	
	@PostMapping("/search/{search_type}")
	@JsonView(value = {Views.JSONProduct_search_result.class})
		public ResponseEntity<SearchProductResult> search(@PathVariable String search_type ,
				@RequestParam String keyWords, @RequestParam int offset, @RequestParam int limit,
				@RequestParam(required = false) String sort,
				@RequestBody(required = false) Map<String, String[]> filters) throws Exception{
				
				out.println("HERE: KEYWORD: " + keyWords);
				Product_SortTypes sortType = null;
				try {
						sortType = Product_SortTypes.valueOf(sort);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				SearchProductResult result = null;
				
				try {
						if(search_type.equals("name")) {
							result = productService.searchByName(keyWords, offset, limit, sortType, filters);
						}
						else {
							result = productService.searchByCategory(keyWords, offset, limit, sortType, filters);
						}
				}catch (Exception e) {
					e.printStackTrace();
					throw new GeneralException("");
				}
				
				return ResponseEntity.status(HttpStatus.OK).body(result);
	}
}

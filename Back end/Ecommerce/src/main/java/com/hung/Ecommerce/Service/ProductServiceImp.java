package com.hung.Ecommerce.Service;

import static java.lang.System.out;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.hung.Ecommerce.DTO.MultipartHolder;
import com.hung.Ecommerce.DTO.SearchProductResult;
import com.hung.Ecommerce.Model.Product;
import com.hung.Ecommerce.Model.ProductOption;
import com.hung.Ecommerce.Model.ProductRating;
import com.hung.Ecommerce.Model.Product_SortTypes;
import com.hung.Ecommerce.Model.StaticResourceMapper;
import com.hung.Ecommerce.Repository.ProductRepository;
import com.hung.Ecommerce.Repository.SelectionOrder;

@Service
public class ProductServiceImp implements ProductService {

private ProductRepository productRepository;
	
	@Value("${external_resource.path.image}")
		private String imageSavingPath;
	
	@Autowired
		SavingFileAssistant fileSaver;

	@Autowired
		public ProductServiceImp(ProductRepository productRepository) {
			super();
			this.productRepository = productRepository;
		}

	@Transactional(readOnly = true)
	@Override
	public Product findById(int id, boolean fetchOrNot) {
		return productRepository.findById(id, fetchOrNot);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Product> findAll(boolean fetchOrNot) {
		return productRepository.findAll(fetchOrNot);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Product> findByProperty(String propertyName, Object value, boolean trueIsAscending_falseIsDescending,
			boolean fetchOrNot) {
		return productRepository.findByProperty(propertyName, value, trueIsAscending_falseIsDescending, fetchOrNot);
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<Product> findManyByProperty(String propertyName, Object[] values,
			boolean trueIsAscending_falseIsDescending, boolean fetchOrNot) {
		return productRepository.findManyByProperty(propertyName, values, trueIsAscending_falseIsDescending, fetchOrNot);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Product> findByProperties(Map<String, Object> conditions, Map<String, SelectionOrder> orders, boolean TrueIsAnd_FalseIsOr, boolean fetchOrNot) throws IllegalArgumentException {
		return productRepository.findByProperties(conditions, orders, TrueIsAnd_FalseIsOr, fetchOrNot);
	}

	@Transactional
	@Override
	public void save(Product theOne) {
		 productRepository.save(theOne);
	}

	@Transactional
	@Override
	public void reconnect(Product product) {
		productRepository.reconnect(product);
	}

	@Transactional
	@Override
	public Product update(Product product) {
		return productRepository.update(product);
	}

	@Transactional
	@Override
	public int updateByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr, String targetField,
			Object newValue) {
		return productRepository.updateByProperties(conditions, TrueIsAnd_FalseIsOr, targetField, newValue);
	}

	@Transactional
	@Override
	public void delete(int id) {
		productRepository.delete(id);
	}

	@Transactional
	@Override
	public int deleteByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr) {
		return productRepository.deleteByProperties(conditions, TrueIsAnd_FalseIsOr);
	}	
	
	@Transactional
	@Override
		public void addImagesToProduct(String productId, MultipartHolder avatarImage, List<MultipartHolder> otherImages,
																List<MultipartHolder> optionImages) {
				List<Product> matchedProduct = findByProperty("id", productId, false, false);
				
				if(!matchedProduct.isEmpty()) {
					Product product = matchedProduct.get(0);
					CompletableFuture<List<StaticResourceMapper>> avatarResult;
					CompletableFuture<List<StaticResourceMapper>> otherImagesResult;
					
					String imagePath = "/StaticResources/Images/Products/";
					 
							try {
									if(avatarImage != null) {
											 avatarResult = fileSaver.saveFiles(new ArrayList<>(List.of(avatarImage)), imageSavingPath + "Products\\", "image");
											 avatarResult.thenAccept( mapperList -> {
												 if(!mapperList.isEmpty()) {
													 product.setAvatarImageURL(imagePath+mapperList.get(0).getFileKey());
														out.println("HERE: SAVED AVATAR");
													}
											 });
											
									}
									
									if(optionImages != null && optionImages.size()> 0) {
											for(ProductOption option: product.getProductOptions()) {
													for(MultipartHolder image: optionImages) {
															if(image.getFileName().equals(option.getImageUrl())) {
																CompletableFuture<List<StaticResourceMapper>> result = fileSaver.saveFiles(new ArrayList<>(List.of(image)),
																																	imageSavingPath + "Products\\", "image");
																result.thenAccept(mapper -> {
																		if(!mapper.isEmpty()) {
																			option.setImageUrl(imagePath+mapper.get(0).getFileKey());
																			out.println("HERE: SAVED " + image.getFileName());
																		}
																});
															}
													}
											}
									}
								
									
									if(otherImages != null && otherImages.size() > 0) {
											otherImagesResult = fileSaver.saveFiles(otherImages, imageSavingPath + "Products\\", "image");
											otherImagesResult.thenAccept(mappers -> {
													if(!mappers.isEmpty()) {
														Stream<StaticResourceMapper> resultStream = mappers.stream();
														List<String> imageKeys = resultStream.map(mapper -> imagePath+mapper.getFileKey()).collect(Collectors.toList());
														product.setOtherImagesURLS(new HashSet<>(imageKeys));
														out.println("HERE: SAVED OTHER");
													}
											});
									}
									
								} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
				}
		}

	
	@Transactional(readOnly = true)
	@Override
	public SearchProductResult searchByName(String keyWords, int offset, int limit, Product_SortTypes sortType
																			, Map<String, String[]> filters) {
		try {
			return productRepository.searchByName(keyWords, offset, limit, sortType, filters);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
			
	}
	
	
	@Transactional(readOnly = true)
	@Override
	public SearchProductResult searchByCategory(String keyWords, int offset, int limit, Product_SortTypes sortType
																				, Map<String, String[]> filters) {
			return productRepository.searchByCategory(keyWords, offset, limit,  sortType, filters);
	}

}

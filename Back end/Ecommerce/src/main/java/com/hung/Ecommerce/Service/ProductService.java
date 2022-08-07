package com.hung.Ecommerce.Service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.hung.Ecommerce.DTO.MultipartHolder;
import com.hung.Ecommerce.DTO.SearchProductResult;
import com.hung.Ecommerce.Model.Product;
import com.hung.Ecommerce.Model.Product_SortTypes;

public interface ProductService extends AppService<Product> {

	public List<Product> findManyByProperty(String propertyName, Object[] values, boolean trueIsAscending_falseIsDescending,
			boolean fetchOrNot);
	
	public SearchProductResult searchByName(String keyWords, int offset, int limit, Product_SortTypes sortType
																			, Map<String, String[]> filters);
	public void addImagesToProduct(String productId, MultipartHolder avatarImage, List<MultipartHolder> otherImages,
			List<MultipartHolder> optionImages);
	public SearchProductResult searchByCategory(String keyWords, int offset, int limit, Product_SortTypes sortType
																				, Map<String, String[]> filters);
}

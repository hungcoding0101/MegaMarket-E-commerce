package com.hung.Ecommerce.Service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hung.Ecommerce.Model.ProductOrder;
import com.hung.Ecommerce.Model.ProductRating;
import com.hung.Ecommerce.Repository.ProductRatingRepository;
import com.hung.Ecommerce.Repository.SelectionOrder;

@Service
@Transactional
public class ProductRatingServiceImp implements ProductRatingService{

private ProductRatingRepository productRatingRepository;
	
	@Autowired
		public ProductRatingServiceImp(ProductRatingRepository productRatingRepository) {
			super();
			this.productRatingRepository = productRatingRepository;
		}

	@Override
	public ProductRating findById(int id, boolean fetchOrNot) {
		return productRatingRepository.findById(id, fetchOrNot);
	}

	@Override
	public List<ProductRating> findAll(boolean fetchOrNot) {
		return productRatingRepository.findAll(fetchOrNot);
	}

	@Override
	public List<ProductRating> findByProperty(String propertyName, Object value, boolean trueIsAscending_falseIsDescending,
			boolean fetchOrNot) {
		return productRatingRepository.findByProperty(propertyName, value, trueIsAscending_falseIsDescending, fetchOrNot);
	}

	@Override
	public List<ProductRating> findByProperties(Map<String, Object> conditions, Map<String, SelectionOrder> orders, boolean TrueIsAnd_FalseIsOr, boolean fetchOrNot) throws IllegalArgumentException {
		return productRatingRepository.findByProperties(conditions, orders, TrueIsAnd_FalseIsOr, fetchOrNot);
	}
	
	@Override
	public void save(ProductRating theOne) {
		 productRatingRepository.save(theOne);
	}

	@Override
	public void reconnect(ProductRating productRating) {
		productRatingRepository.reconnect(productRating);
	}

	@Override
	public ProductRating update(ProductRating productRating) {
		return productRatingRepository.update(productRating);
	}

	@Override
	public int updateByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr, String targetField,
			Object newValue) {
		return productRatingRepository.updateByProperties(conditions, TrueIsAnd_FalseIsOr, targetField, newValue);
	}

	@Override
	public void delete(int id) {
		productRatingRepository.delete(id);
	}

	@Override
	public int deleteByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr) {
		return productRatingRepository.deleteByProperties(conditions, TrueIsAnd_FalseIsOr);
	}
}

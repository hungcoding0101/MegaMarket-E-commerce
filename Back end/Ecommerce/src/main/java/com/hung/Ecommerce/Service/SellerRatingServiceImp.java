package com.hung.Ecommerce.Service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hung.Ecommerce.Model.ProductRating;
import com.hung.Ecommerce.Model.Seller;
import com.hung.Ecommerce.Model.SellerRating;
import com.hung.Ecommerce.Repository.SelectionOrder;
import com.hung.Ecommerce.Repository.SellerRatingRepository;

@Service
@Transactional
public class SellerRatingServiceImp implements SellerRatingService{

	private SellerRatingRepository sellerRatingRepository;
	
	@Autowired
		public SellerRatingServiceImp(SellerRatingRepository sellerRatingRepository) {
			super();
			this.sellerRatingRepository = sellerRatingRepository;
		}

	@Override
	public SellerRating findById(int id, boolean fetchOrNot) {
		return sellerRatingRepository.findById(id, fetchOrNot);
	}

	@Override
	public List<SellerRating> findAll(boolean fetchOrNot) {
		return sellerRatingRepository.findAll(fetchOrNot);
	}

	@Override
	public List<SellerRating> findByProperty(String propertyName, Object value, boolean trueIsAscending_falseIsDescending,
			boolean fetchOrNot) {
		return sellerRatingRepository.findByProperty(propertyName, value, trueIsAscending_falseIsDescending, fetchOrNot);
	}

	@Override
	public List<SellerRating> findByProperties(Map<String, Object> conditions, Map<String, SelectionOrder> orders, boolean TrueIsAnd_FalseIsOr, boolean fetchOrNot) throws IllegalArgumentException {
		return sellerRatingRepository.findByProperties(conditions, orders, TrueIsAnd_FalseIsOr, fetchOrNot);
	}

	@Override
	public void save(SellerRating theOne) {
		 sellerRatingRepository.save(theOne);
	}

	@Override
	public void reconnect(SellerRating sellerRating) {
		sellerRatingRepository.reconnect(sellerRating);
	}

	@Override
	public SellerRating update(SellerRating sellerRating) {
		return sellerRatingRepository.update(sellerRating);
	}

	@Override
	public int updateByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr, String targetField,
			Object newValue) {
		return sellerRatingRepository.updateByProperties(conditions, TrueIsAnd_FalseIsOr, targetField, newValue);
	}

	@Override
	public void delete(int id) {
		sellerRatingRepository.delete(id);
	}

	@Override
	public int deleteByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr) {
		return sellerRatingRepository.deleteByProperties(conditions, TrueIsAnd_FalseIsOr);
	}
}

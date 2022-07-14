package com.hung.Ecommerce.Service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hung.Ecommerce.Model.ProductCategory;
import com.hung.Ecommerce.Model.ProductOption;
import com.hung.Ecommerce.Repository.ProductOptionRepository;
import com.hung.Ecommerce.Repository.SelectionOrder;

@Service
@Transactional
public class ProductOptionServiceImp implements ProductOptionService{

private ProductOptionRepository productOptionRepository;
	
	@Autowired
		public ProductOptionServiceImp(ProductOptionRepository productOptionRepository) {
			super();
			this.productOptionRepository = productOptionRepository;
		}

	@Override
	public ProductOption findById(int id, boolean fetchOrNot) {
		return productOptionRepository.findById(id, fetchOrNot);
	}

	@Override
	public List<ProductOption> findAll(boolean fetchOrNot) {
		return productOptionRepository.findAll(fetchOrNot);
	}

	@Override
	public List<ProductOption> findByProperty(String propertyName, Object value, boolean trueIsAscending_falseIsDescending,
			boolean fetchOrNot) {
		return productOptionRepository.findByProperty(propertyName, value, trueIsAscending_falseIsDescending, fetchOrNot);
	}
	
	@Override
		public List<ProductOption> findManyByProperty(String propertyName, List<Object> values,
												boolean trueIsAscending_falseIsDescending, boolean fetchOrNot) {
				return productOptionRepository.findManyByProperty(propertyName, values, trueIsAscending_falseIsDescending,
						fetchOrNot);
		}

	@Override
	public List<ProductOption> findByProperties(Map<String, Object> conditions, Map<String, SelectionOrder> orders, boolean TrueIsAnd_FalseIsOr, boolean fetchOrNot) throws IllegalArgumentException {
		return productOptionRepository.findByProperties(conditions, orders, TrueIsAnd_FalseIsOr, fetchOrNot);
	}

	@Override
	public void save(ProductOption theOne) {
		 productOptionRepository.save(theOne);
	}

	@Override
	public void reconnect(ProductOption productOption) {
		productOptionRepository.reconnect(productOption);
	}

	@Override
	public ProductOption update(ProductOption productOption) {
		return productOptionRepository.update(productOption);
	}

	@Override
	public int updateByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr, String targetField,
			Object newValue) {
		return productOptionRepository.updateByProperties(conditions, TrueIsAnd_FalseIsOr, targetField, newValue);
	}

	@Override
	public void delete(int id) {
		productOptionRepository.delete(id);
	}

	@Override
	public int deleteByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr) {
		return productOptionRepository.deleteByProperties(conditions, TrueIsAnd_FalseIsOr);
	}
}

package com.hung.Ecommerce.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hung.Ecommerce.Model.Notification;
import com.hung.Ecommerce.Model.ProductCategory;
import com.hung.Ecommerce.Repository.ProductCategoryRepository;
import com.hung.Ecommerce.Repository.SelectionOrder;
import com.hung.Ecommerce.Util.Customtree.Tree;
import com.hung.Ecommerce.Util.Customtree.TreeNode;

@Service
public class ProductCategoryServiceImp implements ProductCategoryService{

private ProductCategoryRepository productCategoryRepository;
	
	@Autowired
		public ProductCategoryServiceImp(ProductCategoryRepository productCategoryRepository) {
			super();
			this.productCategoryRepository = productCategoryRepository;
		}

	@Transactional(readOnly = true)
	@Override
	public ProductCategory findById(int id, boolean fetchOrNot) {
		return productCategoryRepository.findById(id, fetchOrNot);
	}

	@Transactional(readOnly = true)
	@Override
	public List<ProductCategory> findAll(boolean fetchOrNot) {
		return productCategoryRepository.findAll(fetchOrNot);
	}

	@Transactional(readOnly = true)
	@Override
	public List<ProductCategory> findByProperty(String propertyName, Object value, boolean trueIsAscending_falseIsDescending,
			boolean fetchOrNot) {
		return productCategoryRepository.findByProperty(propertyName, value, trueIsAscending_falseIsDescending, fetchOrNot);
	}
	
	@Transactional(readOnly = true)
	@Override
		public List<ProductCategory> findManyByProperty(String propertyName, List<Object> values,
				boolean trueIsAscending_falseIsDescending, boolean fetchOrNot) {
			return productCategoryRepository.findManyByProperty(propertyName, values, trueIsAscending_falseIsDescending, fetchOrNot);
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<ProductCategory> findByProperties(Map<String, Object> conditions, Map<String, SelectionOrder> orders, boolean TrueIsAnd_FalseIsOr, boolean fetchOrNot) throws IllegalArgumentException {
		return productCategoryRepository.findByProperties(conditions, orders, TrueIsAnd_FalseIsOr, fetchOrNot);
	}

	@Transactional
	@Override
	public void save(ProductCategory theOne) {
		 productCategoryRepository.save(theOne);
	}

	@Transactional
	@Override
	public void reconnect(ProductCategory productCategory) {
		productCategoryRepository.reconnect(productCategory);
	}

	@Transactional
	@Override
	public ProductCategory update(ProductCategory productCategory) {
		return productCategoryRepository.update(productCategory);
	}

	@Transactional
	@Override
	public int updateByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr, String targetField,
			Object newValue) {
		return productCategoryRepository.updateByProperties(conditions, TrueIsAnd_FalseIsOr, targetField, newValue);
	}

	@Transactional
	@Override
	public void delete(int id) {
		productCategoryRepository.delete(id);
	}

	@Transactional
	@Override
	public int deleteByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr) {
		return productCategoryRepository.deleteByProperties(conditions, TrueIsAnd_FalseIsOr);
	}

	@Transactional
	@Override
	public void renderProductCategoryTree(TreeNode<ProductCategory> node) {
		Set<ProductCategory> children = node.getElement().getChildren();
		if(children == null || children.isEmpty()) {return;}
		
		else {
			for(ProductCategory child: children) {
				TreeNode<ProductCategory> newNode = new TreeNode<ProductCategory>();
				newNode.setElement(child);
				newNode.setParent(node);
				node.addChild(newNode);
				renderProductCategoryTree(newNode);
			}
		}
	}

	@Transactional(readOnly = true)
	@Override
	public ProductCategory getProductCategoryTree() {
			ProductCategory root = productCategoryRepository.findByProperty("title", "products", true, true).get(0);
			return root;
	}
	
	@Transactional(readOnly = true)
	public List<ProductCategory> searchByDepth(int depth, int offset, int limit){
			return productCategoryRepository.searchByDepth(depth, offset, limit);
	}

	@Transactional(readOnly = true)
	@Override
	public List<String> getSuggestions(String keyWords, int offset, int limit) {
		return productCategoryRepository.getSuggestions(keyWords, offset, limit);
	}
	
}

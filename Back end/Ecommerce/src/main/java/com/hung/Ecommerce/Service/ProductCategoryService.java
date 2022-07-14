package com.hung.Ecommerce.Service;

import java.util.List;

import com.hung.Ecommerce.Model.ProductCategory;
import com.hung.Ecommerce.Util.Customtree.Tree;
import com.hung.Ecommerce.Util.Customtree.TreeNode;

public interface ProductCategoryService extends AppService<ProductCategory> {

	public void renderProductCategoryTree(TreeNode<ProductCategory> node);
	public ProductCategory getProductCategoryTree();
	public List<ProductCategory> findManyByProperty(String propertyName, List<Object> values,
			boolean trueIsAscending_falseIsDescending, boolean fetchOrNot);
	public List<ProductCategory> searchByDepth(int depth, int offset, int limit);
	public List<String> getSuggestions(String keyWords, int offset, int limit);
}

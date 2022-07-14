package com.hung.Ecommerce.Service;

import com.hung.Ecommerce.DTO.RequestToAddToCart;
import com.hung.Ecommerce.Model.CartItem;

public interface CartItemService extends AppService<CartItem> {

		public CartItem addToCart(RequestToAddToCart request);
		public void deleteCart(Integer id, String userName);
}

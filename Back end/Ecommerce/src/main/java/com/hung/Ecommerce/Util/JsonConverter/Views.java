package com.hung.Ecommerce.Util.JsonConverter;

public class Views {

	//Product
	public static interface JSONProduct_public extends JSONProductCategory_public, JSONUser_public, JSONSeller_public,  JSONProductOption_public, JSONProductOrder_public{
	}
	public static interface JSONProduct_private extends JSONProduct_public,  JSONProductOption_private, JSONProductOrder_private{
	}
	
	//Product category
	public static interface JSONProductCategory_public{
	}
	public static interface JSONProductCategory_private extends JSONProductCategory_public{}
	
	//Product option
	public static interface JSONProductOption_public{
	}
	public static interface JSONProductOption_private extends JSONProductOption_public{}
	
	
	// Product order
	public static interface JSONProductOrder_public extends JSONProductOption_public{
	}
	public static interface JSONProductOrder_private extends JSONProductOrder_public{}
	
	//CartItem
	public static interface JSONCartItem_public extends JSONProductOption_public{
	}
	public static interface JSONCartItem_private extends JSONCartItem_public, JSONProductOption_private{}
	
	//User
	public static interface JSONUser_private extends JSONUser_public{
	}
	public static interface JSONUser_public extends JSONNotification_public{
	}
	
	//Seller
	public static interface JSONSeller_private extends JSONSeller_public, JSONUser_private, JSONProductOrder_private{
	}
	public static interface JSONSeller_public extends JSONUser_public, JSONProductOrder_public{
	}
	
	//Customer
	public static interface JSONCustomer_private extends JSONCustomer_public, JSONUser_private, JSONCartItem_private,
		JSONRating_public{
	}
	public static interface JSONCustomer_public extends JSONUser_public, JSONProductOrder_public, JSONProduct_public,
												JSONCartItem_public{
	}
	
	
	//Notification
	public static interface JSONNotification_private extends JSONNotification_public{
	}
	public static interface JSONNotification_public {
	}
	
	//Chat
	public static interface JSONChat_private extends JSONChat_public{
	}
	public static interface JSONChat_public {
	}
	
	//Rating
	public static interface JSONRating_private extends JSONRating_public, JSONComment_private{
	}
	public static interface JSONRating_public extends JSONUser_public, JSONComment_public{
	}
	
	//Seller rating
	public static interface JSONSellerRating_private extends JSONSellerRating_public{
	}
	public static interface JSONSellerRating_public extends JSONRating_public {
	}
	
	//Product rating
	public static interface JSONProductRating_private extends JSONProductRating_public{
	}
	public static interface JSONProductRating_public extends JSONRating_public {
	}
	
	//Comment
	public static interface JSONComment_private extends JSONComment_public{
	}
	public static interface JSONComment_public {
	}
	
	//Order result
	public static interface JSONOrder_result extends JSONProductOrder_public, JSONEntityChanged{
	}

	// EntityChanged exception
	public static interface JSONEntityChanged {
	}
	
	//Order Update result
	public static interface JSONOrder_update_result {		
	}
	
	//Search product result
	public static interface JSONProduct_search_result  extends JSONProduct_public{		
	}
}

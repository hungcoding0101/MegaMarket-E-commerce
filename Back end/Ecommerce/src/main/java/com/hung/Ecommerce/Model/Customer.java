package com.hung.Ecommerce.Model;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.hung.Ecommerce.Util.JsonConverter.Views;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Indexed
@NamedEntityGraph(name = "customer.fetch.1",
	attributeNodes = {@NamedAttributeNode(value = "deliveryAddresses"),
								@NamedAttributeNode(value = "orders", subgraph = "orders"),
								@NamedAttributeNode(value = "cart", subgraph = "cart"),
								@NamedAttributeNode(value = "purchasedProducts"),
								@NamedAttributeNode(value = "productRatings", subgraph = "productRatings"),
								@NamedAttributeNode(value = "sellerRatings", subgraph = "sellerRatings"),
								@NamedAttributeNode(value = "notifications", subgraph = "notifications"),
								@NamedAttributeNode(value = "chats", subgraph = "chats"),
	} ,

	subgraphs = {
		@NamedSubgraph(name = "cart", attributeNodes = {@NamedAttributeNode("product"), @NamedAttributeNode("choices")
		}),
		@NamedSubgraph(name = "orders", attributeNodes = {@NamedAttributeNode("product"), 
				@NamedAttributeNode("choices"), @NamedAttributeNode("customer"), @NamedAttributeNode("seller")
		}),
		@NamedSubgraph(name = "productRatings", attributeNodes = {@NamedAttributeNode("comment"),
				@NamedAttributeNode("images"), @NamedAttributeNode("product")
		}),
		@NamedSubgraph(name = "sellerRatings", attributeNodes = {@NamedAttributeNode("seller"), 
				@NamedAttributeNode("images"), @NamedAttributeNode("comment")
		}),
		@NamedSubgraph(name = "notifications", attributeNodes = {@NamedAttributeNode("targetUser")}),
		@NamedSubgraph(name = "chats", attributeNodes = {@NamedAttributeNode("users")}),
	}
)
public class Customer extends User {

	@ElementCollection(targetClass =  String.class)
	@JsonView(value = {Views.JSONCustomer_private.class})
	@EqualsAndHashCode.Exclude
	@GenericField(sortable = Sortable.YES)
		private Set<String> deliveryAddresses;
	
	@OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
	@JsonView(value = {Views.JSONCustomer_private.class})
	@OrderBy(value = "createdDate DESC")
	@JsonIgnoreProperties(value = {"customer"})
	@EqualsAndHashCode.Exclude
		private Set<CartItem> cart;
	
	@OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
	@JsonView(value = {Views.JSONCustomer_private.class})
	@OrderBy(value = "orderDate DESC")
//	@JsonIdentityReference(alwaysAsId=true)
	@EqualsAndHashCode.Exclude
	@IndexedEmbedded(includePaths = {"id_sort"})
		private Set<ProductOrder> orders;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JsonView(value = {Views.JSONCustomer_private.class})
	@EqualsAndHashCode.Exclude
	@JsonIdentityReference(alwaysAsId=true)
	@IndexedEmbedded(includePaths = {"id_sort"})
		private Set<Product> purchasedProducts;
	
	@OneToMany(mappedBy = "creator", fetch = FetchType.LAZY)
	@JsonView(value = {Views.JSONCustomer_private.class})
	@OrderBy(value = "sentDate DESC")
	@JsonIgnoreProperties(value = {"creator"})
	@EqualsAndHashCode.Exclude
		private Set<ProductRating> productRatings;
	
	@OneToMany(mappedBy = "creator", fetch = FetchType.LAZY)
	@JsonView(value = {Views.JSONCustomer_private.class})
	@OrderBy(value = "sentDate DESC")
	@EqualsAndHashCode.Exclude
		private Set<SellerRating> sellerRatings;
	
	//------------------------------------------------------------
	public void addCartItem(CartItem c) {
		this.cart.add(c);
		c.setCustomer(this);
	}
	
	public void addCartItem(List<CartItem> cs) {
		for(CartItem c: cs) {
			addCartItem(c);
		}
	}
	
	public void removeCartItem(CartItem c) {
		c.setCustomer(null);
		this.cart.remove(c);
	}
	
	public void removeCartItems() {
		 Iterator<CartItem> iterator = this.cart.iterator();
		 while (iterator.hasNext()) {
			 CartItem c = iterator.next();
			 c.setCustomer(null);
			 iterator.remove();
		 }
	}

//---------------------------------------------------------------------------
	public void addPurchasedProduct(Product product) {
		this.purchasedProducts.add(product);
		product.getPurchasedCustomers().add(this);
	}
	
	public void addPurchasedProducts(List<Product> products) {
		for(Product product: products) {
			addPurchasedProduct(product);
		}
	}
	
	public void removePurchasedProduct(Product product) {
		product.getPurchasedCustomers().remove(this);
		this.purchasedProducts.remove(product);
	}
	
	public void removePurchasedProducts() {
		 Iterator<Product> iterator = this.purchasedProducts.iterator();
		 while (iterator.hasNext()) {
			 Product product = iterator.next();
			 product.getPurchasedCustomers().remove(this);
			 iterator.remove();
		 }
	}		

	//---------------------------------------------------------------------------
		public void addProductOrder(ProductOrder order) {
			this.orders.add(order);
			order.setCustomer(this);
		}
		
		public void addProducOrders(List<ProductOrder> oders) {
			for(ProductOrder order: orders) {
				addProductOrder(order);
			}
		}
		
		public void removeProductOrder(ProductOrder order) {
			order.setProduct(null);
			this.orders.remove(order);
		}
		
		public void removeProductOrders() {
			 Iterator<ProductOrder> iterator = this.orders.iterator();
			 while (iterator.hasNext()) {
				 ProductOrder order = iterator.next();
				 order.setCustomer(null);
				 iterator.remove();
			 }
		}		
	
}

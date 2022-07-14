package com.hung.Ecommerce.Model;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.hung.Ecommerce.Util.JsonConverter.Views;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Indexed
@NamedEntityGraph(name = "seller.fetch.1",
	attributeNodes = {@NamedAttributeNode(value = "notifications"),
								@NamedAttributeNode(value = "orders", subgraph = "orders"),
								@NamedAttributeNode(value = "ownedProducts"),
								@NamedAttributeNode(value = "requests"),
								@NamedAttributeNode(value = "receivedRatings"),
								@NamedAttributeNode(value = "chats", subgraph = "chats"),
	} ,

	subgraphs = {
		@NamedSubgraph(name = "orders", attributeNodes = {@NamedAttributeNode("product"), 
				@NamedAttributeNode("choices"), @NamedAttributeNode("customer"),
		}),
	}
)
public class Seller extends User implements RatedTarget{
	
	@OneToMany(mappedBy = "seller", fetch = FetchType.LAZY)
	@OrderBy(value = "LastModificationTime DESC")
	@JsonIdentityReference(alwaysAsId=true)
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@JsonView(value = {Views.JSONSeller_public.class})
	@IndexedEmbedded(includePaths = {"id_sort"})
		private Set<Product> ownedProducts;
	
//	@OneToMany(mappedBy = "seller")
//		private List<Offer> offers;
	
	@OneToMany(mappedBy = "seller", fetch = FetchType.LAZY)
	@OrderBy(value = "orderDate DESC")
	@JsonView(value = {Views.JSONSeller_private.class})
//	@JsonIdentityReference(alwaysAsId=true)
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@IndexedEmbedded(includePaths = {"id_sort"})
		private Set<ProductOrder> orders;
	
	@OneToMany(mappedBy = "sender", fetch = FetchType.LAZY)
	@JsonView(value = {Views.JSONSeller_private.class})
	@OrderBy(value = "sentDate DESC")
	@JsonIdentityReference(alwaysAsId=true)
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
//	@IndexedEmbedded(includePaths = {"id_sort"})
		private Set<AbstractRequest> requests;
	
	@OneToMany(mappedBy = "seller")
	@OrderBy(value = "sentDate")
	@JsonIdentityReference(alwaysAsId=true)
	@JsonView(value = {Views.JSONSeller_public.class})
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
//	@IndexedEmbedded(includePaths = {""})
		private Set<SellerRating> receivedRatings;
	
	
//----------------------------------------------------------------------------------			
	public void addProduct(Product product) {
		this.ownedProducts.add(product);
		product.setSeller(this);
	}
	
	public void addProducts(List<Product> products) {
		for(Product product: products) {
				addProduct(product);
		}
	}
	
	public void removeProduct(Product product) {
		product.setSeller(null);
		this.ownedProducts.remove(product);
	}
	
	public void removeProducts() {
		 Iterator<Product> iterator = this.ownedProducts.iterator();
		 while (iterator.hasNext()) {
			 Product product = iterator.next();
		 product.setSeller(null);
		 iterator.remove();
		
		 }
	}

	
//----------------------------------------------------------------------------------		
	public void addProductOrder(ProductOrder order) {
		this.orders.add(order);
		order.setSeller(this);
	}
	
	public void addProducOrders(List<ProductOrder> oders) {
		for(ProductOrder order: orders) {
			addProductOrder(order);
		}
	}
	
	public void removeProductOrder(ProductOrder order) {
		order.setSeller(null);
		this.orders.remove(order);
	}
	
	public void removeProductOrders() {
		 Iterator<ProductOrder> iterator = this.orders.iterator();
		 while (iterator.hasNext()) {
			 ProductOrder order = iterator.next();
			 order.setSeller(null);
			 iterator.remove();
		 }
	}	
	
//----------------------------------------------------------------------------------			
	@Override
	public String toString() {
		return "Seller [name= " + this.getUsername()+ "]";
	}
	
	
}

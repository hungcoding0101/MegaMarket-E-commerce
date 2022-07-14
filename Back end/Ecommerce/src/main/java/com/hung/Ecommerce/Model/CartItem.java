package com.hung.Ecommerce.Model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyClass;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.hung.Ecommerce.CustomAnnotation.ProperProductPrice;
import com.hung.Ecommerce.Util.JsonConverter.Views;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Indexed
@NamedEntityGraph(name = "cart.fetch.1",
attributeNodes = {@NamedAttributeNode(value = "product"),
							@NamedAttributeNode(value = "customer"),
							@NamedAttributeNode(value = "choices"),
})

public class CartItem {

	@JsonView(value = {Views.JSONCartItem_private.class})
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Id
	@GenericField(sortable = Sortable.YES, name = "id_sort")
	private Integer id;	
	
		
	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull(message = "{Validation.Presence}")
	@JsonView(value = {Views.JSONCartItem_private.class})
	@JsonIdentityReference(alwaysAsId=true)
	@IndexedEmbedded(includePaths = {"id_sort", "name_sort"})
		private Product product;
	
	@NotNull(message = "{Validation.Presence}")
	@Min(value = 0)
		private Long productVersion;
		
	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull(message = "{Validation.Presence}")
	@JsonIgnore
	@IndexedEmbedded(includePaths = {"id_sort", "name_sort"})
		private Customer customer;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JsonView(value = {Views.JSONCartItem_private.class})
	@Cascade(CascadeType.DELETE)
		private Set<ProductOption> choices;
	
	@ElementCollection(targetClass = Long.class)
	@MapKeyClass(Integer.class)
	@Cascade(CascadeType.DELETE)
		private Map<Integer, Long> choices_versions;
	
	@Min (value = 1, message = "{Validation.Minimum.Number}")
	@NotNull(message = "{Validation.Presence}")
	@JsonView(value = {Views.JSONCartItem_private.class})
	@GenericField(sortable = Sortable.YES)
		private int quantity;
	
	@NotNull(message = "{Validation.Presence.Price}")
	@JsonView(value = {Views.JSONCartItem_private.class})
	@ProperProductPrice
		private Double unitPrice;
	
	@NotNull(message = "{Validation.Presence}")
	@ProperProductPrice
	@JsonView(value = {Views.JSONCartItem_private.class})
		private double totalPrice;
	
	@NotNull(message = "{Validation.Presence}")
	@JsonView(value = {Views.JSONCartItem_private.class})
	@ProperProductPrice
		private Double deliveryFeePerUnit; 
	
	@NotNull(message = "{Validation.Presence}")
	@JsonView(value = {Views.JSONCartItem_private.class})
	@ProperProductPrice
		private Double maxDeliveryFee;
	
	@PastOrPresent
	@GenericField(sortable = Sortable.YES)
		private LocalDateTime createdDate;
	
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
		updateTotalPrice();
	}
	
	// This method is call to set up totalPrice based on order quantity and product price
	public Double updateTotalPrice() {
		Double unitDeliveryFee = this.getProduct().getDeliveryFeePerUnit();
		Double maxDeliveryFee = this.getProduct().getMaxDeliveryFee();
		Double finalDeliveryFee = 0d;
		Double priceDiff = this.choices != null 
										? choices.stream().map(option -> option.getPriceDifference()).reduce(Double::sum).orElse(0.0)
										: 0.0;
		Double unitPrice =  this.product.getDefaultUnitPrice() + priceDiff;
			
		if(unitDeliveryFee * this.quantity <= maxDeliveryFee) {
			finalDeliveryFee = unitDeliveryFee * this.quantity;
		}
		else {
			finalDeliveryFee = maxDeliveryFee;
		}
		
		this.unitPrice = unitPrice;
		
		 this.totalPrice = unitPrice * this.quantity + finalDeliveryFee;
		 
		 return this.totalPrice;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		result = prime * result + ((productVersion == null) ? 0 : productVersion.hashCode());
		long temp;
		temp = Double.doubleToLongBits(totalPrice);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CartItem other = (CartItem) obj;
		if (createdDate == null) {
			if (other.createdDate != null)
				return false;
		} else if (!createdDate.equals(other.createdDate))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		if (productVersion == null) {
			if (other.productVersion != null)
				return false;
		} else if (!productVersion.equals(other.productVersion))
			return false;
		if (Double.doubleToLongBits(totalPrice) != Double.doubleToLongBits(other.totalPrice))
			return false;
		return true;
	}
	
	
}

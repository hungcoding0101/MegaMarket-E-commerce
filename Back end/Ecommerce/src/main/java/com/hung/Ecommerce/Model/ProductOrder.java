package com.hung.Ecommerce.Model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyClass;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Version;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Immutable;
import org.hibernate.search.engine.backend.types.Searchable;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.hung.Ecommerce.CustomAnnotation.ProperProductPrice;
import com.hung.Ecommerce.Util.JsonConverter.Views;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Indexed
public class ProductOrder {

	@Id @GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	@JsonView(value = {Views.JSONProductOrder_public.class})
	@GenericField(sortable = Sortable.YES, name = "id_sort")
		private String id; 
	
	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull(message = "{Validation.Presence}")
	@JsonIdentityReference(alwaysAsId=true)
	@JsonView(value = {Views.JSONProductOrder_public.class})
	@IndexedEmbedded(includePaths = {"id_sort"})
		private Product product;
	
	
	@Min (value = 1, message = "{Validation.Minimum.Number}")
	@NotNull(message = "{Validation.Presence}")
	@JsonView(value = {Views.JSONProductOrder_public.class})
	@GenericField(sortable = Sortable.YES)
		private int quantity;
	
	@ProperProductPrice
	@NotNull(message = "{Validation.Presence}")
	@JsonView(value = {Views.JSONProductOrder_public.class})
		private Double finalPrice;
	
	@NotBlank
	@JsonView(value = {Views.JSONProductOrder_public.class})
		private String paymentMethod;

	@ManyToMany(fetch = FetchType.LAZY)
	@JsonView(value = {Views.JSONProductOrder_public.class})
		private Set<ProductOption> choices;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonView(value = {Views.JSONProductOrder_public.class})
	@JsonIncludeProperties(value = {"username"})
	@IndexedEmbedded(includePaths = {"id_sort", "name_sort"})
		private Customer customer;
	
	@JsonView(value = {Views.JSONProductOrder_public.class})
		private String phoneNumber;
	
	@JsonView(value = {Views.JSONProductOrder_public.class})
	@FullTextField
		private String deliveryAddress;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonView(value = {Views.JSONProductOrder_public.class})
	@JsonIncludeProperties(value = {"username"})
	@IndexedEmbedded(includePaths = {"id_sort", "name_sort"})
		private Seller seller;
	
	
	@Enumerated(EnumType.STRING)
	@JsonView(value = {Views.JSONProductOrder_public.class})
	@GenericField(sortable = Sortable.YES)
		private ProductOrderStatus status;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm, dd-MM-yyyy")
	@PastOrPresent
	@JsonView(value = {Views.JSONProductOrder_public.class})
	@GenericField(sortable = Sortable.YES)
		private LocalDateTime orderDate;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm, dd-MM-yyyy")
	@JsonView(value = {Views.JSONProductOrder_public.class})
	@GenericField(sortable = Sortable.YES)
		private LocalDateTime deliveryDate;
	
	
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
		updateFinalPrice();
	}
	
	// This method is call to set up totalPrice based on order quantity and product price
	public Double updateFinalPrice() {
		Double unitDeliveryFee = this.getProduct().getDeliveryFeePerUnit();
		Double maxDeliveryFee = this.getProduct().getMaxDeliveryFee();
		Double finalDeliveryFee = 0d;
		Double priceDiff = this.choices != null 
										? choices.stream().map(option -> option.getPriceDifference()).reduce(Double::sum).orElse(0.0)
										: 0.0;
		Double unitPrice =  this.product.getDefaultUnitPrice() + priceDiff;
		
		
		if(unitDeliveryFee * this.quantity <= maxDeliveryFee) {finalDeliveryFee = unitDeliveryFee * this.quantity;}
		else {finalDeliveryFee = maxDeliveryFee;}
		
		 this.finalPrice = unitPrice * this.quantity + finalDeliveryFee;
		 
		 return this.finalPrice;
	}

	@Override
	public String toString() {
		return "ProductOrder [id=" + id + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductOrder other = (ProductOrder) obj;
		if (finalPrice == null) {
			if (other.finalPrice != null)
				return false;
		} else if (!finalPrice.equals(other.finalPrice))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (orderDate == null) {
			if (other.orderDate != null)
				return false;
		} else if (!orderDate.equals(other.orderDate))
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		if (quantity != other.quantity)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((finalPrice == null) ? 0 : finalPrice.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((orderDate == null) ? 0 : orderDate.hashCode());
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		result = prime * result + quantity;
		return result;
	}
	
	
}

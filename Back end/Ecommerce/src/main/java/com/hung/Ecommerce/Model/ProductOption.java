package com.hung.Ecommerce.Model;

import java.util.List;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.OptimisticLock;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.hung.Ecommerce.CustomAnnotation.ProperProductPrice;
import com.hung.Ecommerce.CustomExceptions.ForbiddenActionException;
import com.hung.Ecommerce.Util.JsonConverter.Views;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductOption {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@JsonView(value = {Views.JSONProductOption_public.class})
		private Integer id;
	
	
	@Version
	@JsonView(value = {Views.JSONProductOption_public.class})
		private Long version;
	
	//@Pattern(regexp = "[.]{2,}" , message = "{Validation.Format.ProductName}")
	@JsonView(value = {Views.JSONProductOption_public.class})
	@NotBlank
		private String name;
	
	@JsonView(value = {Views.JSONProductOption_public.class})
	@Enumerated(EnumType.STRING)
	@NotNull
		private ProductOptionStatus status;
	
	@ManyToOne
	@NotNull(message = "{Validation.Presence}")
	@JsonIdentityReference(alwaysAsId=true)
	@JsonView(value = {Views.JSONProductOption_public.class})
	@JsonIgnore
		private Product product;
	
//	@OneToMany(mappedBy = "choice", fetch = FetchType.LAZY)
//	@JsonView(value = {Views.JSONProductOption_private.class})
//	private Set<ProductOrder> orders;
	
	@JsonView(value = {Views.JSONProductOption_public.class})
	private String characteristic;
	
	@NotNull(message = "{Validation.Presence.Price}")
	@JsonView(value = {Views.JSONProductOption_public.class})
	@ProperProductPrice
		private Double priceDifference;
	
	@NotNull(message = "{Validation.Presence.Price}")
	@JsonView(value = {Views.JSONProductOption_public.class})
	@Min(0)
		private int stock;
	
	@OptimisticLock(excluded = true)
	@Min(0)
	@JsonView(value = {Views.JSONProductOption_public.class})
		private int sales;
	
	@OptimisticLock(excluded = true)
	@JsonView(value = {Views.JSONProductOption_public.class})
	private String imageUrl;
	

	public int getStock() {
		return stock;
	}

	public void setStock(int newStock) throws IllegalArgumentException {			
		if(newStock < 0) {
			throw new IllegalArgumentException("Stock must not be smaller than 0");
		}
		this.stock = newStock;
	}
	
	public void setSales(int newSales) throws IllegalArgumentException{
		if(newSales < 0) {
			throw new IllegalArgumentException("Sales must not be smaller than 0");
		}	
		this.sales = newSales;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((imageUrl == null) ? 0 : imageUrl.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((product == null) ? 0 : product.hashCode());
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
		ProductOption other = (ProductOption) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (imageUrl == null) {
			if (other.imageUrl != null)
				return false;
		} else if (!imageUrl.equals(other.imageUrl))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		return true;
	}
	
	
}

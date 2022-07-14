package com.hung.Ecommerce.Model;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyClass;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Version;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OptimisticLock;
import org.hibernate.search.engine.backend.types.Aggregable;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.hung.Ecommerce.CustomAnnotation.ProperProductPrice;
import com.hung.Ecommerce.CustomExceptions.CharacteristicIntegrityException;
import com.hung.Ecommerce.Util.JsonConverter.Views;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@AllArgsConstructor
@Getter
@Setter
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "id")
@Indexed
@NamedEntityGraph(name = "product.fetch.1",
	attributeNodes = {@NamedAttributeNode(value = "categories"),
			@NamedAttributeNode(value = "seller"),
			@NamedAttributeNode(value = "receivedRatings", subgraph = "receivedRatings"),
			@NamedAttributeNode(value = "characteristics"),
			@NamedAttributeNode(value = "productOptions"),
			@NamedAttributeNode(value = "otherImagesURLS"),
			@NamedAttributeNode(value = "specificDetails"),
			@NamedAttributeNode(value = "orders", subgraph = "orders"),
			@NamedAttributeNode(value = "paymentMethods"),
	} ,
	
	subgraphs = {
			@NamedSubgraph(name = "receivedRatings", attributeNodes = {@NamedAttributeNode("images")}),
			@NamedSubgraph(name = "orders", attributeNodes = {@NamedAttributeNode("choices")}),
	})
	
public class Product implements RatedTarget{

	@JsonView(value = {Views.JSONProduct_public.class})
	@Id @GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	@KeywordField(sortable = Sortable.YES, name = "id_sort")
		private String id;

	@Version
	@JsonView(value = {Views.JSONProduct_public.class})
		private Long version;
	
	
	@OptimisticLock(excluded = true)
	@ManyToMany(fetch = FetchType.LAZY)
	@OrderBy(value = "depth ASC")
	@NotEmpty(message = "{Validation.Presence.ProductCategory}")
	@Valid
	@JsonView(value = {Views.JSONProduct_public.class})
	@JsonIncludeProperties(value = {"id", "title"})
	@EqualsAndHashCode.Exclude
	@IndexedEmbedded(includePaths = {"title_sort", "id_sort", "title"})
		private Set<ProductCategory> categories;
	
	
	
	@EqualsAndHashCode.Exclude
	@JsonView(value = {Views.JSONProduct_public.class})
	@JsonIncludeProperties(value = {"username", "id"})
	@OptimisticLock(excluded = true)
	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull
	@ToString.Exclude
	@IndexedEmbedded(includePaths = {"id_sort", "name_sort"})
		private Seller seller;
	

	@OptimisticLock(excluded = true)
	@JsonView(value = {Views.JSONProduct_public.class})
	@Column(columnDefinition = "TEXT")
		private String productDescription;
	

	@OptimisticLock(excluded = true)
	@JsonView(value = {Views.JSONProduct_public.class})
	@ElementCollection(targetClass =  String.class)
	@EqualsAndHashCode.Exclude
		private Set<String> characteristics;
	
	@OneToMany(mappedBy = "product",  cascade= CascadeType.ALL, orphanRemoval = true)
	@OptimisticLock(excluded = true)
	@JsonView(value = {Views.JSONProduct_public.class})
	@OrderBy(value = "name ASC")
	@Valid
	@EqualsAndHashCode.Exclude
		private Set<ProductOption> productOptions;
	
//	@ManyToMany
//		private Set<Offer> offers;
	
	@OptimisticLock(excluded = true)
	@JsonView(value = {Views.JSONProduct_public.class})
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
	@OrderBy(value = "sentDate DESC")
	@EqualsAndHashCode.Exclude
	@IndexedEmbedded(includePaths = {"score"})
		private Set<ProductRating> receivedRatings;
	
	@OptimisticLock(excluded = true)
	@JsonView(value = {Views.JSONProduct_private.class})
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "purchasedProducts")
	@JsonIncludeProperties(value = {"username", "phoneNumber", "email"})
	@EqualsAndHashCode.Exclude
	@JsonIgnore
	@IndexedEmbedded(includePaths = {"id_sort", "name_sort"})
		private Set<Customer> purchasedCustomers;
	
	
	@NotBlank(message = "{Validation.Format.ProductName}")
	@JsonView(value = {Views.JSONProduct_public.class})
	@FullTextField
	@KeywordField(name = "name_sort", projectable = Projectable.YES, sortable = Sortable.YES)
		private String name;
	
	@OneToMany(mappedBy = "product")
	@OptimisticLock(excluded = true)
	@Valid
	@EqualsAndHashCode.Exclude
	@IndexedEmbedded(includePaths = {"id_sort", "quantity"})
		private Set<CartItem> cartItems;
	
	
	@OneToMany(mappedBy = "product")
	@JsonView(value = {Views.JSONProduct_private.class})
	@JsonIncludeProperties(value = {"id"})
	@OptimisticLock(excluded = true)
	@Valid
	@EqualsAndHashCode.Exclude
	@IndexedEmbedded(includePaths = {"id_sort", "quantity"})
		private Set<ProductOrder> orders;

	
	@NotNull(message = "{Validation.Presence.Price}")
	@JsonView(value = {Views.JSONProduct_public.class})
	@ProperProductPrice
	@GenericField(sortable = Sortable.YES)
		private Double defaultUnitPrice;
	
	// Delivery fee for per unit, being multiplied as order quantity increases
	@NotNull(message = "{Validation.Presence.Price}")
	@JsonView(value = {Views.JSONProduct_public.class})
	@ProperProductPrice
		private Double deliveryFeePerUnit; 
	
	
	// Maximum delivery fee per order
	@NotNull(message = "{Validation.Presence.Price}")
	@JsonView(value = {Views.JSONProduct_public.class})
	@ProperProductPrice
		private Double maxDeliveryFee;
	
	@JsonView(value = {Views.JSONProduct_public.class})
	@FullTextField
	@KeywordField(sortable = Sortable.YES, name = "brand_sort", aggregable = Aggregable.YES)
		private String brand;
	
	
	@Enumerated(EnumType.STRING)
	@NotNull
	@GenericField
		private ProductStatus status;
	
	@JsonView(value = {Views.JSONProduct_public.class})
	@OptimisticLock(excluded = true)
		private String avatarImageURL;
	
	
	@JsonView(value = {Views.JSONProduct_public.class})
	@OptimisticLock(excluded = true)
	@ElementCollection(targetClass =  String.class)
	@EqualsAndHashCode.Exclude
		private Set<String> otherImagesURLS;
	
	
	@JsonView(value = {Views.JSONProduct_public.class})
	@Min(0)
	@GenericField(sortable = Sortable.YES)
		private int totalStock;
	
	
	@JsonView(value = {Views.JSONProduct_public.class})
	@OptimisticLock(excluded = true)
	@Min(0)
	@GenericField(sortable = Sortable.YES)
		private int totalSales;
	

	@JsonView(value = {Views.JSONProduct_public.class})
	@NotBlank(message = "{Validation.Presence.ProductUnit}")
		private String unit;
	
	
	@JsonView(value = {Views.JSONProduct_public.class})
	@OptimisticLock(excluded = true)
	@NotNull(message = "{Validation.Presence.DeliveryTime}")
		private String estimatedDeliveryTime;
	
	
	@JsonView(value = {Views.JSONProduct_public.class})
	@OptimisticLock(excluded = true)
	@Min(0)
	@GenericField(sortable = Sortable.YES)
		private double averageRatingScore;
	
	@JsonView(value = {Views.JSONProduct_private.class})
	@OptimisticLock(excluded = true)
	@PastOrPresent
	@GenericField(sortable = Sortable.YES)
		private LocalDateTime LastModificationTime;
	
	
	@OptimisticLock(excluded = true)
	@JsonView(value = {Views.JSONProduct_public.class})
	@ElementCollection(targetClass = String.class)
	@MapKeyClass(String.class)
	@EqualsAndHashCode.Exclude
		private Map<String, String> specificDetails;
	
	
	@JsonView(value = {Views.JSONProduct_public.class})
	@ElementCollection(targetClass =  String.class)
		private Set<String> paymentMethods;
	
//----------------------------------------------------------------------------------		
	public void setTotalStock(int newStock, Set<ProductOption> options) throws CharacteristicIntegrityException {
			if(newStock < 0) {
				throw new IllegalArgumentException("Stock must not be smaller than 0");
			}
		
			if(!(options == null || options.isEmpty())) {
				for(ProductOption option: options) {
					if(!this.productOptions.contains(option)) {
						throw new IllegalArgumentException("Illegal options");
					}
				}	
				
				int gap = newStock - this.totalStock;
				
				for(String characteristic: this.getCharacteristics()) {
					ProductOption optionWithThisCharacteristic = options.stream().filter(option -> option.getCharacteristic()
							.equals(characteristic)).findFirst().
							orElseThrow(() -> new IllegalArgumentException("Illegal options"));		
					
					if(optionWithThisCharacteristic.getStock() + gap < 0) {
						throw new IllegalArgumentException("There is only " + optionWithThisCharacteristic.getStock()
						+ " items for option " + optionWithThisCharacteristic.getId());
					}

					optionWithThisCharacteristic.setStock(optionWithThisCharacteristic.getStock() + gap);
				}
			}
			
			this.totalStock = newStock;
	}
	
	public void setTotalSales(int newSales, Set<ProductOption> options) throws CharacteristicIntegrityException{
			if(newSales < 0) {
				throw new IllegalArgumentException("Sales must not be smaller than 0");
			}
		
			if(!(options == null || options.isEmpty())) {
				for(ProductOption option: options) {
					if(!this.productOptions.contains(option)) {
						throw new IllegalArgumentException("Illegal options");
					}
				}	
				
				int gap = newSales - this.totalSales;
				
				for(String characteristic: this.getCharacteristics()) {
					ProductOption optionWithThisCharacteristic = options.stream().filter(option -> option.getCharacteristic()
							.equals(characteristic)).findFirst().
							orElseThrow(() -> new IllegalArgumentException("Illegal options"));		
					
					if(optionWithThisCharacteristic.getStock() + gap < 0) {
						throw new IllegalArgumentException("There is only " + optionWithThisCharacteristic.getStock()
						+ " items for option " + optionWithThisCharacteristic.getId());
					}
		
					optionWithThisCharacteristic.setSales(optionWithThisCharacteristic.getSales() + gap);
				}
			}
			
			this.totalSales = newSales;
	}
		
//---------------------------------------------------------------------------------
	public Product() {
		super();
		this.productOptions = new HashSet<>();
		this.categories = new HashSet<ProductCategory>();
		this.purchasedCustomers = new HashSet<Customer>();
		this.receivedRatings = new HashSet<>();
		this.totalStock = 0;
	}
	
//----------------------------------------------------------------------------------			
	public void addProductOption(ProductOption option) {
		this.productOptions.add(option);
		option.setProduct(this);
	}
	
	public void addProductOptions(Set<ProductOption> options) {
		for(ProductOption option: options) {
			addProductOption(option);
		}
	}
	
	public void removeProductOption(ProductOption option) {
		option.setProduct(null);
		this.productOptions.remove(option);
	}
	
	public void removeProductOptions() {
		 Iterator<ProductOption> iterator = this.productOptions.iterator();
		 while (iterator.hasNext()) {
			 ProductOption option = iterator.next();
			 option.setProduct(null);
			 iterator.remove();
		 }
	}

//----------------------------------------------------------------------------------		
	public void addCategory(ProductCategory c) {
		this.categories.add(c);
		c.getProducts().add(this);
	}
	
	public void addCategories(Set<ProductCategory> cs) {
		for(ProductCategory c: cs) {
			addCategory(c);
		}
	}
	
	public void removeCategory(ProductCategory c) {
		c.getProducts().remove(this);
		this.categories.remove(c);
	}
	
	public void removeCategories() {
		 Iterator<ProductCategory> iterator = this.categories.iterator();
		 while (iterator.hasNext()) {
			 ProductCategory c = iterator.next();
			 c.getProducts().remove(this);
			 iterator.remove();
		 }
	}

//----------------------------------------------------------------------------------		
	public void addRating(ProductRating r) {
		this.receivedRatings.add(r);
		r.setProduct(this);
	}
	
	public void addRatings(List<ProductRating> rs) {
		for(ProductRating r: rs) {
			addRating(r);
		}
	}
	
	public void removeRating(ProductRating r) {
		r.setProduct(null);
		this.receivedRatings.remove(r);
	}
	
	public void removeRatings() {
		 Iterator<ProductRating> iterator = this.receivedRatings.iterator();
		 while (iterator.hasNext()) {
			 ProductRating r = iterator.next();
			 r.setProduct(null);
			 iterator.remove();
		 }
	}
	
//---------------------------------------------------------------------------------
	
	public void addProductOrder(ProductOrder order) {
		this.orders.add(order);
		order.setProduct(this);
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
			 order.setProduct(null);
			 iterator.remove();
		 }
	}	
	
//----------------------------------------------------------------------------------		
	public void addCustomer(Customer c) {
		this.purchasedCustomers.add(c);
		c.getPurchasedProducts().add(this);
	}
	
	public void addCustomers(List<Customer> cs) {
		for(Customer c: cs) {
			addCustomer(c);
		}
	}
	
	public void removeCustomer(Customer c) {
		c.getPurchasedProducts().remove(this);
		this.purchasedCustomers.remove(c);
	}
	
	public void removeCustomers() {
		 Iterator<Customer> iterator = this.purchasedCustomers.iterator();
		 while (iterator.hasNext()) {
			 Customer c = iterator.next();
			 c.getPurchasedProducts().remove(this);
			 iterator.remove();
		 }
	}
	

//----------------------------------------------------------------------------------		
	public void addCartItem(CartItem c) {
				this.cartItems.add(c);
				c.setProduct(this);
	}
			
	public void addCartItems(List<CartItem> cs) {
				for(CartItem c: cs) {
					addCartItem(c);
				}
	}
			
	public void removeCartItem(CartItem c) {
				c.setProduct(null);
				this.cartItems.remove(c);
	}
			
	public void removeCartItem() {
				 Iterator<CartItem> iterator = this.cartItems.iterator();
				 while (iterator.hasNext()) {
					 CartItem c = iterator.next();
					 c.setProduct(null);;
					 iterator.remove();
	}
}
	
	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name +"unit price" +defaultUnitPrice + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((avatarImageURL == null) ? 0 : avatarImageURL.hashCode());
		result = prime * result + ((brand == null) ? 0 : brand.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((unit == null) ? 0 : unit.hashCode());
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
		Product other = (Product) obj;
		if (avatarImageURL == null) {
			if (other.avatarImageURL != null)
				return false;
		} else if (!avatarImageURL.equals(other.avatarImageURL))
			return false;
		if (brand == null) {
			if (other.brand != null)
				return false;
		} else if (!brand.equals(other.brand))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (unit == null) {
			if (other.unit != null)
				return false;
		} else if (!unit.equals(other.unit))
			return false;
		return true;
	}
}

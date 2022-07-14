package com.hung.Ecommerce.Model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.hung.Ecommerce.Util.JsonConverter.View_ProductCategory_all;
import com.hung.Ecommerce.Util.JsonConverter.View_ProductCategory_categories;
import com.hung.Ecommerce.Util.JsonConverter.Views;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@NamedEntityGraph(name = "category.fetch.1",
	attributeNodes = {
			@NamedAttributeNode(value = "products"),
			@NamedAttributeNode(value = "parrent"),
			@NamedAttributeNode(value = "children", subgraph = "children"),
	},

	subgraphs = {
			@NamedSubgraph(name = "children", attributeNodes = {@NamedAttributeNode("children")})
	}
)
@Indexed
public class ProductCategory {

	@Id
	@JsonView(value = {Views.JSONProductCategory_public.class})
	@GeneratedValue(strategy =  GenerationType.SEQUENCE)
	@GenericField(sortable = Sortable.YES, name = "id_sort")
		private Integer id;
	
	@JsonIdentityReference(alwaysAsId=true)
	@JsonView(value = {Views.JSONProductCategory_private.class})
	@ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
	@IndexedEmbedded(includePaths = {"id_sort"})
		private Set<Product> products;
	
	@Column(unique = true)
	@NotBlank
	@JsonView(value = {Views.JSONProductCategory_public.class})
	@FullTextField(projectable = Projectable.YES)
	@KeywordField(sortable = Sortable.YES, name = "title_sort", projectable = Projectable.YES)
		private String title;
	
	@JsonView(value = {Views.JSONProductCategory_public.class})
	private String value = title;
	
	
	@Min(value = 0, message = "Depth < 0")
	@JsonView(value = {Views.JSONProductCategory_public.class})
	@GenericField(sortable = Sortable.YES)
	@NotNull
		private int depth;
	
	
	@ManyToOne()
	@JsonView(value = {Views.JSONProductCategory_public.class})
	@JsonIncludeProperties(value = {"id", "title"})
	@IndexedEmbedded(includePaths = {"title_sort", "id_sort"})
		private ProductCategory parrent;
	
	 @OneToMany(mappedBy="parrent", fetch = FetchType.EAGER)
	 @JsonView(value = {Views.JSONProductCategory_public.class})
	 @JsonIncludeProperties(value = {"id", "title", "children", "value"})
	 @IndexedEmbedded(includePaths = {"title_sort", "id_sort"})
	  	private Set<ProductCategory> children = new HashSet<ProductCategory>();
	 
	 @JsonView(value = {Views.JSONProductCategory_public.class})
	 	private String avatarImage;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}



	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public ProductCategory getParrent() {
		return parrent;
	}

	public void setParrent(ProductCategory parrent) {
		this.parrent = parrent;
	}

	public Set<ProductCategory> getChildren() {
		return children;
	}

	public void setChildren(Set<ProductCategory> nodes) {
		this.children = nodes;
	}

	public String getTitle() {
		return title;
	}

	public void setLabel(String title) {
		this.title = title;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
		public int hashCode() {
			return Objects.hash(this.id, this.title, this.depth);
		}
	
	@Override
	public boolean equals(Object otherObject) {
		if(this == otherObject) {return true;}

		if(otherObject == null) {return false;}
		
		if(this.getClass() != otherObject.getClass()) {return false;}
		
		ProductCategory other = (ProductCategory) otherObject;
		
		return this.id == other.id && this.title == other.title && this.depth == other.depth;
	}

	@Override
	public String toString() {
		return "ProductCategory [id=" + id + ", children=" + children + "]";
	}
	  
	
}

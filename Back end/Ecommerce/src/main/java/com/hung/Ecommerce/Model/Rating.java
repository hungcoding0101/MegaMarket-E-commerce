package com.hung.Ecommerce.Model;

import java.time.LocalDateTime;
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
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;

import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.hung.Ecommerce.Util.JsonConverter.Views;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@EqualsAndHashCode()
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "id")
@Data
@Indexed
public abstract class Rating {

	@Id
	@JsonView(value = {Views.JSONRating_public.class})
		private Integer id;
	
	@Enumerated(EnumType.STRING)
	@JsonView(value = {Views.JSONRating_public.class})
	@NotNull
	@GenericField
		private RatingType type;
	
	@ManyToOne
	@JsonView(value = {Views.JSONRating_public.class})
	@JsonIncludeProperties(value = {"username"})
	@NotNull
	@IndexedEmbedded(includePaths = {"id_sort", "name_sort"})
		private Customer creator;
	
	@MapsId
	@OneToOne(fetch = FetchType.LAZY)
	@JsonView(value = {Views.JSONRating_public.class})
		private Comment comment;
	
	@PastOrPresent
	@JsonView(value = {Views.JSONRating_public.class})
	@GenericField(sortable = Sortable.YES)
		private LocalDateTime sentDate;
	
	@ElementCollection(targetClass =  String.class, fetch = FetchType.LAZY)
	@NotEmpty(message = "{Validation.Presence}")
	@JsonView(value = {Views.JSONRating_public.class})
		private Set<String> images;
	
	@Pattern(regexp = "^[\\w ]*$" , message = "{Validation.Format.ProductName}")
	@JsonView(value = {Views.JSONRating_public.class})
		private String subject;
	
	@NotBlank(message = "{Validation.Presence}")
	@Min(value = 1, message = "{Validation.Range.RatingScore}")
	@Max(value = 5, message = "{Validation.Range.RatingScore}")
	@JsonView(value = {Views.JSONRating_public.class})
	@GenericField(sortable = Sortable.YES)
		private int score;
}

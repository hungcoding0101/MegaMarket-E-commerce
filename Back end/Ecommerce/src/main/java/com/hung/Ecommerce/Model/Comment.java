package com.hung.Ecommerce.Model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.Immutable;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import io.micrometer.core.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "id")
public class Comment {

	@Id
	@GeneratedValue(strategy =  GenerationType.SEQUENCE)
		private Integer id;
	
	@ManyToOne
	@NonNull
	@JsonIncludeProperties(value = {"username"})
		private Customer creator;
	
	@Pattern(regexp = "^[\\w][\\w ]*$" , message = "{Validation.Format.ProductName}")
		private String contents;
	
	@PastOrPresent
		private LocalDateTime createdDate;
	
}

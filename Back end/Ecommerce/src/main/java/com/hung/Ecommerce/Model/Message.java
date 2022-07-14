package com.hung.Ecommerce.Model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode()
@Data
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "id")
public class Message {

	@Id
		private Integer id;	
	
	@Version
		private Long version;
	
	@ManyToOne
		private Chat chat;
	
	@ManyToOne
		private User sender;
	
	private String content;
	
	@ElementCollection(targetClass = String.class)
		private List<String> files;
	
	private LocalDateTime dateSent;
}

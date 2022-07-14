package com.hung.Ecommerce.Model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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
public class Chat {

	@Id
		private Integer id;
	
	@Version
		private Long version;
	
	@ManyToMany
	@JsonIncludeProperties(value = {"username"})
		private Set<User> users;
	
	@OneToMany(mappedBy = "chat", fetch = FetchType.LAZY)
		private List<Message> messages;
	
	private LocalDateTime lastTimeModified;
}

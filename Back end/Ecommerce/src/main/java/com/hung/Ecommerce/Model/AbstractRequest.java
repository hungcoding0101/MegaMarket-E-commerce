package com.hung.Ecommerce.Model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NoArgsConstructor
@AllArgsConstructor
@Data
public abstract class AbstractRequest {

	@Id
		private Integer id;
	
	@ManyToOne
		private User sender;
	
	@ManyToOne
		private User receiver;
	
	@Enumerated(EnumType.STRING)
		private RequestStatus status;
	
	private LocalDateTime sentDate;
}

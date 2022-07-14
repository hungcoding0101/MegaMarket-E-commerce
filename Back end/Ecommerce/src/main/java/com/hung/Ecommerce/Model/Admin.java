package com.hung.Ecommerce.Model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class Admin extends User{

	@OneToMany(mappedBy = "receiver")
		private List<AbstractRequest> receivedRequests;
}

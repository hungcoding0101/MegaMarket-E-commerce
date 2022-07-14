package com.hung.Ecommerce.DTO;

import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.hung.Ecommerce.Model.AbstractRequest;
import com.hung.Ecommerce.Model.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class UploadProductRequest extends AbstractRequest {

	@ManyToOne
		private Product product;
	
	private String message;
	
	private String reasonOfReject;
	
}

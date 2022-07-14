package com.hung.Ecommerce.CustomExceptions;

import com.fasterxml.jackson.annotation.JsonView;
import com.hung.Ecommerce.Util.JsonConverter.Views;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class EntityChangedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonView(value = {Views.JSONEntityChanged.class})
	private String changedFieldName;
	
	@JsonView(value = {Views.JSONEntityChanged.class})
	private String reason;

	public EntityChangedException(String message) {
		super(message);
	}

	public EntityChangedException(String changedFieldName, String reason) {
		super();
		this.changedFieldName = changedFieldName;
		this.reason = reason;
	}

	
}

package com.hung.Ecommerce.CustomExceptions;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public class InvalidUserInfoException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Map<String, String> violations;
	
	public Map<String, String> getViolations(){
		return this.violations;
	}

}

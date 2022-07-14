package com.hung.Ecommerce.CustomExceptions;

public class InvalidFileTypeException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public InvalidFileTypeException(String errorMessage) {
	      super(errorMessage);
	  }
}

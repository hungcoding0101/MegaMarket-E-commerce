package com.hung.Ecommerce.CustomExceptions;

public class IncorrectFileExtensionException extends Exception {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public IncorrectFileExtensionException(String errorMessage) {
      super(errorMessage);
  }
}

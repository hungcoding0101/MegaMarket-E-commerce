package com.hung.Ecommerce.CustomExceptions;

public class ForbiddenActionException extends Exception {

	private static final long serialVersionUID = 1L;

	public ForbiddenActionException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ForbiddenActionException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public ForbiddenActionException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ForbiddenActionException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ForbiddenActionException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
	
}

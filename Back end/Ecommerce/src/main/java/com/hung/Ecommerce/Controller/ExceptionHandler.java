package com.hung.Ecommerce.Controller;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.hung.Ecommerce.CustomExceptions.GeneralException;
import com.hung.Ecommerce.DTO.ErrorObject;

@RestControllerAdvice
public class ExceptionHandler {
	

	@org.springframework.web.bind.annotation.ExceptionHandler(value = GeneralException.class)
		public ResponseEntity<ErrorObject> generalHandler(Exception e, WebRequest request){
			e.printStackTrace();
			String responseBody = "Some unknown errors occurred. Please try again later";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
					.body(new ErrorObject("server error", responseBody, request.getContextPath()));
		}
	
	@org.springframework.web.bind.annotation.ExceptionHandler(value = MethodArgumentNotValidException.class)
		public ResponseEntity<ErrorObject> javaxBindingHandler(MethodArgumentNotValidException e, WebRequest request){
			e.printStackTrace();
			String responseBody = e.getFieldError().getDefaultMessage();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
					.body(new ErrorObject("Bad request", responseBody, request.getContextPath()));
		}
	
	@org.springframework.web.bind.annotation.ExceptionHandler(MaxUploadSizeExceededException.class)
		public ResponseEntity<String> maxFileSizeHandler(MaxUploadSizeExceededException e){
			return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(e.getMessage());
	}
		

//	@org.springframework.web.bind.annotation.ExceptionHandler(ConstraintViolationException.class)
//	public ResponseEntity<String> POJOConstraintHandler(ConstraintViolationException e){
//		
//		Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
//		StringBuilder builder = new StringBuilder(e.getMessage());
//		for(ConstraintViolation<?> violation: violations) {
//			builder.append(violation.getPropertyPath() + ": " + violation.getMessage() + "\n");
//		}
//		
//		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(builder.toString());
//	}
}

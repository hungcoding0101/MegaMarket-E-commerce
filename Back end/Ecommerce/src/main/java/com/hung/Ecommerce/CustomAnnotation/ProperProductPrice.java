package com.hung.Ecommerce.CustomAnnotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.springframework.context.annotation.PropertySource;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ProperProductPriceImp.class)
@Documented
@PropertySource("classpath:messages.properties")
public @interface ProperProductPrice {

	 String message() default "{Validation.Format.Price}"; 
	 
	 Class<?>[] groups() default{};
		
	 public abstract Class<? extends Payload>[] payload()default {};
}

package com.hung.Ecommerce.CustomAnnotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ProperProductPriceImp implements ConstraintValidator<ProperProductPrice, Double>{

	@Override
	public boolean isValid(Double value, ConstraintValidatorContext context) {
			if(value != null) {
				return value == 0 || (value >= 500 && value%500 == 0);
			}
			
			return true;
	}
}

package com.springboot.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.springboot.utils.ValidationUtils;

public class EmailValidator implements ConstraintValidator<Email, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		System.out.println("email="+value);
		if(value==null || value.length()<=0) {
			return false;
		}
		return ValidationUtils.isValidEmail(value);
	}

}

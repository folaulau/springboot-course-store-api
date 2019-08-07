package com.springboot.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.springboot.utils.ValidationUtils;

public class PasswordValidator implements ConstraintValidator<Password, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {	
		if(value==null) {
			return false;
		}
		return ValidationUtils.isValidPassword(value);
	}

}

package com.springboot.validator;

import java.util.Date;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.springboot.utils.DateTimeUtils;

public class FutureStartDateValidator implements ConstraintValidator<FutureStartDate, Date> {

	@Override
	public boolean isValid(Date value, ConstraintValidatorContext context) {
		
		return DateTimeUtils.isValidStartDate(value);
	}

}

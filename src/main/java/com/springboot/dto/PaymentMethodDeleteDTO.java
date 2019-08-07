package com.springboot.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.springboot.validator.NotEmptyString;

@JsonInclude(value = Include.NON_NULL)
public class PaymentMethodDeleteDTO {

	@NotEmptyString
	private String uid;

	public PaymentMethodDeleteDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

}

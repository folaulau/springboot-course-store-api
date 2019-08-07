package com.springboot.paymentgateway;

import java.util.Map;

import com.springboot.user.User;

public interface PaymentGatewayService {

	com.stripe.model.Customer create(User user, Map<String,Object> metadata);
	
	com.stripe.model.Customer update(User user, Map<String,Object> metadata);
}

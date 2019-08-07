package com.springboot.paymentgateway;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.paymentgateway.customer.CustomerService;
import com.springboot.user.User;
import com.stripe.model.Customer;

@Service
public class PaymentGatewayServiceImp implements PaymentGatewayService {

	@Autowired
	private CustomerService customerService;
	
	@Override
	public Customer create(User user, Map<String, Object> metadata) {
		// TODO Auto-generated method stub
		return customerService.create(user, metadata);
	}

	@Override
	public Customer update(User user, Map<String, Object> metadata) {
		// TODO Auto-generated method stub
		return customerService.update(user, metadata);
	}

}

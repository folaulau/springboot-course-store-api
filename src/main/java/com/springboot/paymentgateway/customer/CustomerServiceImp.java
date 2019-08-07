package com.springboot.paymentgateway.customer;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.springboot.user.User;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;

@Service
public class CustomerServiceImp implements CustomerService {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${stripe.secret.key}")
	private String STRIPE_API_KEY;

	@Override
	public Customer create(User user, Map<String,Object> metadata) {
		log.info("create(..)");
		Stripe.apiKey = STRIPE_API_KEY;

		Map<String, Object> customerParams = new HashMap<String, Object>();
		customerParams.put("description", "Monomono Customer");
		customerParams.put("email", user.getEmail());
		customerParams.put("name", user.getName());
		
		if(user.getPhoneNumber()!=null && user.getPhoneNumber().length()>0) {
			customerParams.put("phone", user.getPhoneNumber());
		}
		
		if(user.getAddress()!=null) {
			Map<String,Object> address = user.getAddress().getPaymentGatewayAddress();
			
			if(address!=null) {
				customerParams.put("address", address);
			}
		}
		
		com.stripe.model.Customer customer = null;
		
		try {
			customerParams.put("metadata", metadata);
			customer = com.stripe.model.Customer.create(customerParams);
		} catch (StripeException e) {
			log.warn(e.getLocalizedMessage());
			e.printStackTrace();
		} catch (Exception e) {
			log.warn(e.getLocalizedMessage());
			e.printStackTrace();
		}
		
		return customer;
	}

	@Override
	public Customer update(User user, Map<String, Object> metadata) {
		log.info("create(..)");
		Stripe.apiKey = STRIPE_API_KEY;

		Map<String, Object> customerParams = new HashMap<String, Object>();
		customerParams.put("description", "Monomono Customer");
		customerParams.put("email", user.getEmail());
		customerParams.put("name", user.getName());
		
		if(user.getPhoneNumber()!=null && user.getPhoneNumber().length()>0) {
			customerParams.put("phone", user.getPhoneNumber());
		}
		
		Map<String,Object> address = user.getAddress().getPaymentGatewayAddress();
		
		if(address!=null) {
			customerParams.put("address", address);
		}
		
		com.stripe.model.Customer customer = null;
		
		try {
			customerParams.put("metadata", metadata);
			customer = com.stripe.model.Customer.retrieve(user.getPaymentGatewayId());
			customer = customer.update(customerParams);
		} catch (StripeException e) {
			log.warn(e.getLocalizedMessage());
			e.printStackTrace();
		} catch (Exception e) {
			log.warn(e.getLocalizedMessage());
			e.printStackTrace();
		}
		
		return customer;
	}

}

package com.springboot.paymentgateway.charge;

import java.util.Map;

import com.springboot.payment.Payment;
import com.stripe.model.Charge;

public interface ChargeService {

	Charge chargeOrderPayment(String customerPaymentGateyId, Payment payment, Map<String,Object> metadata);
	
	Charge chargeOrderPaymentWithToken(Payment payment, Map<String,Object> metadata);
}

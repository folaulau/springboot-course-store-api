package com.springboot.payment;

import com.springboot.order.Order;
import com.springboot.paymentmethod.PaymentMethod;

public interface PaymentService {

	Payment payOrder(Order order, PaymentMethod paymentMethod);
	
	Payment payOrder(String paymentGatewayId, Order order, PaymentMethod paymentMethod);
}

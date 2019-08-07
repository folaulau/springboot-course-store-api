package com.springboot.payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.dto.EntityMapper;
import com.springboot.order.Order;
import com.springboot.paymentgateway.charge.ChargeService;
import com.springboot.paymentmethod.PaymentMethod;
import com.springboot.utils.MathUtils;
import com.springboot.utils.ObjectUtils;
import com.stripe.model.Charge;

@Service
public class PaymentServiceImp implements PaymentService {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private EntityMapper entityMapper;
	
	@Autowired
	private PaymentRespository paymentRespository;
	
	@Autowired
	private ChargeService chargeService;
	
	private Payment create(Payment payment) {
		return paymentRespository.saveAndFlush(payment);
	}

	@Override
	public Payment payOrder(Order order, PaymentMethod paymentMethod) {
		log.debug("payOrder(..)");
		
		Payment payment = new Payment();
		payment.setType(PaymentType.MONOMONO_ORDER);
		payment.setDescription("Order Payment");
		payment.setPaymentMethod(this.entityMapper.mapPaymentMethodToOrderPaymentMethod(paymentMethod));
		payment.setOrderId(order.getId());
		payment.setAmountPaid(order.getTotal());
		
		Charge charge = chargeService.chargeOrderPaymentWithToken(payment, PaymentUtils.generateOrderPaymentMetadata(payment));
		
		payment.setPaid(charge.getPaid());
		payment.setAmountPaid(MathUtils.getDollarsFromCents(charge.getAmount()));
		payment.setStripeChargeId(charge.getId());
		
		payment = create(payment);
		
		log.debug("payment={}", ObjectUtils.toJson(payment));
		
		return payment;
	}

	@Override
	public Payment payOrder(String customerPaymentGatewayId, Order order, PaymentMethod paymentMethod) {
		log.debug("payOrder(..)");
		
		Payment payment = new Payment();
		payment.setType(PaymentType.MONOMONO_ORDER);
		payment.setDescription("Order Payment");
		payment.setPaymentMethod(this.entityMapper.mapPaymentMethodToOrderPaymentMethod(paymentMethod));
		payment.setOrderId(order.getId());
		payment.setAmountPaid(order.getTotal());
		
		Charge charge = chargeService.chargeOrderPayment(customerPaymentGatewayId, payment, PaymentUtils.generateOrderPaymentMetadata(payment));
		
		payment.setPaid(charge.getPaid());
		payment.setAmountPaid(MathUtils.getDollarsFromCents(charge.getAmount()));
		payment.setStripeChargeId(charge.getId());
		
		payment = create(payment);
		
		log.debug("payment={}", ObjectUtils.toJson(payment));
		
		return payment;
	}
	
	
}

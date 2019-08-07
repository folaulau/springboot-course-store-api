package com.springboot.paymentmethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.paymentgateway.card.CardService;
import com.springboot.utils.ApiSessionUtils;
import com.springboot.utils.RandomGeneratorUtils;

@Service
public class PaymentMethodServiceImp implements PaymentMethodService {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private PaymentMethodRepository paymentMethodRepository;
	
	@Autowired
	private CardService cardService;

	@Override
	public PaymentMethod create(PaymentMethod paymentMethod) {
		log.debug("create(...)");
		paymentMethod.setId(new Long(0));
		paymentMethod.setUid(RandomGeneratorUtils.getPaymentMethodUid());
		return paymentMethodRepository.saveAndFlush(paymentMethod);
	}

	@Override
	public PaymentMethod update(PaymentMethod paymentMethod) {
		log.debug("update(...)");
		if(paymentMethod.getId()==null || paymentMethod.getUid()==null) {
			return create(paymentMethod);
		}
		return paymentMethodRepository.saveAndFlush(paymentMethod);
	}

	@Override
	public PaymentMethod getById(Long id) {
		// TODO Auto-generated method stub
		return paymentMethodRepository.findById(id).orElse(null);
	}

	@Override
	public PaymentMethod getByUid(String uid) {
		// TODO Auto-generated method stub
		return paymentMethodRepository.findByUid(uid);
	}

	@Override
	public List<PaymentMethod> add(Long memberId, String paymentGatewayId, PaymentMethod paymentMethod) {
		
		
		paymentMethod.setUser(ApiSessionUtils.getUser(memberId));
		Map<String,Object> metadata = new HashMap<>();
		
		cardService.add(paymentGatewayId, paymentMethod.getSourceToken(), metadata);
		
		create(paymentMethod);
		
		return this.getByCustomerId(memberId);
	}

	@Override
	public List<PaymentMethod> remove(Long memberId, PaymentMethod paymentMethod) {
		log.debug("remove(...)");
		paymentMethod.setDeleted(true);
		
		paymentMethod = update(paymentMethod);

		return this.getByCustomerId(memberId);
	}

	@Override
	public List<PaymentMethod> update(Long memberId, PaymentMethod currentPaymentMethod, PaymentMethod newPaymentMethod) {
		log.debug("update(...)");
		currentPaymentMethod.setDeleted(true);
		this.update(currentPaymentMethod);
		
		this.create(newPaymentMethod);
		
		return this.getByCustomerId(memberId);
	}

	@Override
	public List<PaymentMethod> getByCustomerId(Long memberId) {
		// TODO Auto-generated method stub
		return paymentMethodRepository.findByUserId(memberId);
	}

	@Override
	public List<PaymentMethod> getByCustomerUid(String customerUid) {
		// TODO Auto-generated method stub
		return paymentMethodRepository.findByUserUid(customerUid);
	}
}

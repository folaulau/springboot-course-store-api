package com.springboot.paymentmethod;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.paymentmethod.PaymentMethod;

import java.lang.String;
import java.util.List;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

	PaymentMethod findByUid(String uid);
	
	List<PaymentMethod> findByUserId(Long id);
	
	List<PaymentMethod> findByUserUid(String customerUid);
}

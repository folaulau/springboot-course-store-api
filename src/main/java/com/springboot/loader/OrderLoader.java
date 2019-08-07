package com.springboot.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.springboot.dto.EntityMapper;
import com.springboot.dto.OrderReadDTO;
import com.springboot.order.Order;
import com.springboot.order.OrderService;
import com.springboot.order.lineitem.LineItem;
import com.springboot.paymentmethod.PaymentMethod;
import com.springboot.paymentmethod.PaymentMethodType;
import com.springboot.product.Product;
import com.springboot.user.User;
import com.springboot.utils.ObjectUtils;

@Component
public class OrderLoader {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private EntityMapper entityMapper;
	
	public void load() throws InterruptedException {
		//loadOrders();
	}
	
	
	public void loadOrders() throws InterruptedException {
		log.info("loadOrders...");
		
		Product smallMonomono = new Product();
		smallMonomono.setId(new Long(1));
		
		log.debug("smallMonomono={}",ObjectUtils.toJson(smallMonomono));
		
		Order order = new Order();
		LineItem item1 = new LineItem();
		item1.setCount(2);
		item1.setProduct(smallMonomono);
		item1.setOrder(order);
		
		order.addLineItem(item1);
		
		User customer = new User();
		customer.setId(new Long(1));
		order.setCustomer(customer);
		
		PaymentMethod paymentMethod = new PaymentMethod();
		paymentMethod.setId(new Long(1));
		paymentMethod.setType(PaymentMethodType.CARD);
		
		//order.setPaymentMethod(entityMapper.mapPaymentMethodToOrderPaymentMethod(paymentMethod));
		
		order = this.orderService.create(order);
		
		log.debug("smallMonomono order={}",ObjectUtils.toJson(order));
		
		OrderReadDTO orderReadDTO = entityMapper.mapOrderToOrderReadDTO(order);
		
		log.debug("orderReadDTO={}",ObjectUtils.toJson(orderReadDTO));
		
	}
}

package com.springboot.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springboot.order.Order;

import java.lang.String;

public interface OrderRepository extends JpaRepository<Order, Long> {

	Order findByUid(String uid);
	
	Page<Order> findByCustomerUid(String customerUid, Pageable pageable);
	
//	@Query(nativeQuery=true,value="SELECT cusOrder.uid FROM customer_order as cusOrder JOIN user as customer ON customer.id = cusOrder.customer_id WHERE customer.id = :customerId AND cusOrder.paid = 'F' ORDER BY cusOrder.created_at DESC limit 1")
//	String getLatestOrderUidByCustomerId(@Param("customerId") Long customerId);
//	
//	@Query(nativeQuery=true,value="SELECT * FROM customer_order as cusOrder JOIN user as customer ON customer.id = cusOrder.customer_id WHERE customer.id = :customerId AND cusOrder.paid = 'F' ORDER BY cusOrder.created_at DESC limit 1")
//	Order getLatestOrderByCustomerId(@Param("customerId") Long customerId);
	
	@Query("select order from Order order where order.paid = false and order.current = true and order.customer.id = :customerId ")
	Order getCurrentOrderByCustomerId(@Param("customerId") Long customerId);
}

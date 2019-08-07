package com.springboot.order.lineitem;

import org.springframework.data.jpa.repository.JpaRepository;
import java.lang.String;

import com.springboot.order.lineitem.LineItem;
import com.springboot.product.Product;

import java.util.List;

public interface LineItemRepository extends JpaRepository<LineItem, Long> {

	LineItem findByUid(String uid);
	
	LineItem findByUidAndOrderUid(String uid, String orderUid);
	

	LineItem findByUidAndOrderId(String uid, Long orderId);
	
	LineItem findByProductUidAndOrderUid(String productUid, String orderUid);
}

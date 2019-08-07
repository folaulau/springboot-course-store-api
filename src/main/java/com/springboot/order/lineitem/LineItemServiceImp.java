package com.springboot.order.lineitem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LineItemServiceImp implements LineItemService {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private LineItemRepository lineItemRepository;
	
	@Override
	public LineItem getLineItemByUid(String uid) {
		// TODO Auto-generated method stub
		return lineItemRepository.findByUid(uid);
	}

	@Override
	public LineItem getLineItemByUidAndOrderUid(String uid, String orderUid) {
		// TODO Auto-generated method stub
		return lineItemRepository.findByUidAndOrderUid(uid, orderUid);
	}

	@Override
	public LineItem getLineItemByUidAndOrderId(String uid, Long orderId) {
		// TODO Auto-generated method stub
		return this.lineItemRepository.findByUidAndOrderId(uid, orderId);
	}

	@Override
	public LineItem getLineItemByOrderUidAndProductUid(String orderUid, String productUid) {
		// TODO Auto-generated method stub
		return lineItemRepository.findByProductUidAndOrderUid(productUid, orderUid);
	}

	

}

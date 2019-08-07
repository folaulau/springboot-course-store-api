package com.springboot.order.paymentmethod;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@Embeddable
public class OrderPaymentMethod implements Serializable {

	@Transient
	@JsonIgnore
	private Logger log = LoggerFactory.getLogger(this.getClass());

	private static final long serialVersionUID = 1L;
	
	@Column(name = "payment_method_id")
	private Long id;

	@Column(name = "payment_gateway_id")
	private String paymentGatewayId;
	
	@Column(name = "source_token")
	private String sourceToken;
	
	@Column(name = "payment_method_type")
	private String type;

	@Column(name = "payment_method_name")
	private String name;

	@Column(name = "payment_method_last4")
	private String last4;

	@Column(name = "payment_method_brand")
	private String brand;

	public OrderPaymentMethod() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLast4() {
		return last4;
	}

	public void setLast4(String last4) {
		this.last4 = last4;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getPaymentGatewayId() {
		return paymentGatewayId;
	}

	public void setPaymentGatewayId(String paymentGatewayId) {
		this.paymentGatewayId = paymentGatewayId;
	}

	public String getSourceToken() {
		return sourceToken;
	}

	public void setSourceToken(String sourceToken) {
		this.sourceToken = sourceToken;
	}
}

package com.springboot.order;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.springboot.address.Address;
import com.springboot.order.lineitem.LineItem;
import com.springboot.order.paymentmethod.OrderPaymentMethod;
import com.springboot.payment.Payment;
import com.springboot.paymentmethod.PaymentMethod;
import com.springboot.product.Product;
import com.springboot.user.User;
import com.springboot.utils.MathUtils;
import com.springboot.utils.ObjectUtils;

@JsonInclude(value = Include.NON_NULL)
@Where(clause = "deleted = 'F'")
@Entity
@Table(name = "customer_order")
public class Order implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonIgnore
	@Transient
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false, unique = true)
	private Long id;
	
	@Column(name = "uid", unique = true, nullable=false, updatable=false)
	private String uid;
	
	@OneToMany(orphanRemoval = true, cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy="order")
    private Set<LineItem> lineItems;
	
	@JsonIgnoreProperties(value = { "orders"})
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH })
	@JoinColumn(name = "customer_id", nullable=true)
	private User customer;
	
	@OneToOne(fetch=FetchType.EAGER, cascade=CascadeType.DETACH)
	@JoinColumn(name="payment_id")
	private Payment payment;
	
	// can be admin
	@JsonIgnoreProperties(value = { "orders"})
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH })
	@JoinColumn(name = "helper_user_id", nullable=true)
	private User helper;
	
	@OneToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name="location_address_id")
	private Address location;
	
	@Type(type = "true_false")
	@Column(name = "delivered", nullable = false)
	private boolean delivered;
	
	@Type(type = "true_false")
	@Column(name = "current", nullable = false)
	private boolean current;
	
	@Column(name = "delivered_at")
	private Date deliveredAt;
	
	@Type(type = "true_false")
	@Column(name = "deleted", nullable = false)
	private boolean deleted;
	
	@Column(name = "deleted_at")
	private Date deletedAt;
	
	@Type(type = "true_false")
	@Column(name = "paid", nullable = false)
	private boolean paid;
	
	@Column(name = "paid_at")
	private Date paidAt;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", updatable = false, nullable = false)
	private Date createdAt;

	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at", updatable = true, nullable = false)
	private Date updatedAt;
	
	@Column(name = "total")
	private double total;
	
	public Order() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public double getTotal() {
		this.total = 0;
		if(this.lineItems!=null && this.lineItems.size()>0) {
			this.lineItems.forEach((lineItem)->{
				if(lineItem!=null) {
					this.total += lineItem.getTotal();
				}
			});
			//log.debug("total={}", this.total);
			this.total = MathUtils.getTwoDecimalPlaces(this.total);
			//log.debug("rounded total={}", this.total);
		}
		return this.total;
	}

	public void setTotal(double total) {
		this.total = total;
		
	}

	public User getCustomer() {
		return customer;
	}

	public void setCustomer(User customer) {
		this.customer = customer;
	}

	public boolean isCurrent() {
		return current;
	}

	public void setCurrent(boolean current) {
		this.current = current;
	}

	public User getHelper() {
		return helper;
	}

	public void setHelper(User helper) {
		this.helper = helper;
	}

	public Address getLocation() {
		return location;
	}

	public void setLocation(Address location) {
		this.location = location;
	}

	public boolean isDelivered() {
		return delivered;
	}
	
	public int getTotalItemCount() {
		int totalItemCount = 0;
		if (this.lineItems != null) {
			for(LineItem lineItem : this.lineItems) {
				totalItemCount += lineItem.getCount();
			}
		}
		return totalItemCount;
	}

	public void setDelivered(boolean delivered) {
		this.delivered = delivered;
	}

	public Date getDeliveredAt() {
		return deliveredAt;
	}

	public void setDeliveredAt(Date deliveredAt) {
		this.deliveredAt = deliveredAt;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Date getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(Date deletedAt) {
		this.deletedAt = deletedAt;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	
	
	public Set<LineItem> getLineItems() {
		return lineItems;
	}

	public void setLineItems(Set<LineItem> lineItems) {
		this.lineItems = lineItems;
	}
	
	public void stampPayment(Payment payment) {
		this.payment = payment;
		this.paid=payment.getPaid();
		this.paidAt=new Date();
	}
	
	public void addLineItem(LineItem lineItem) {
		if(this.lineItems == null){
			this.lineItems = new HashSet<>();
		}
		this.lineItems.add(lineItem);
	}
	
	public LineItem getLineItem(Product product) {
		LineItem lineItem = null;
		if(this.lineItems != null){
			for(LineItem lI : this.lineItems) {
				if(product.equals(lI.getProduct())) {
					lineItem = lI;
					break;
				}
			}
		}
		return lineItem;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public boolean isPaid() {
		return paid;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}

	public Date getPaidAt() {
		return paidAt;
	}

	public void setPaidAt(Date paidAt) {
		this.paidAt = paidAt;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.id).append(this.uid).toHashCode();

		// return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		Order other = (Order) obj;
		return new EqualsBuilder().append(this.id, other.id)
				.append(this.uid, other.uid).isEquals();
	}

	public void removeLineItem(LineItem lineItem) {
		lineItem.setDeleted(true);
		lineItem.setDeletedAt(new Date());
		this.lineItems.remove(lineItem);
	}
	
	
//	@PostLoad
//	private void postLoad() {
//		log.debug("postLoad...");
//		if(this.lineItems!=null) {
//			lineItems.forEach((lineItem)->{
//				this.total += lineItem.getTotal();
//			});
//			//log.debug("total={}", this.total);
//			this.total = MathUtils.getTwoDecimalPlaces(this.total);
//			//log.debug("rounded total={}", this.total);
//		}
//		
//	}
	
	@PrePersist
	private void preCreate() {
		this.current = true;
	}
}

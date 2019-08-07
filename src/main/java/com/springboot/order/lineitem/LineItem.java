package com.springboot.order.lineitem;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.springboot.order.Order;
import com.springboot.product.Product;
import com.springboot.utils.MathUtils;
import com.springboot.utils.ObjectUtils;
import com.springboot.utils.RandomGeneratorUtils;

@JsonInclude(value = Include.NON_NULL)
@Where(clause = "deleted = 'F'")
@Entity
@Table(name = "order_line_item")
public class LineItem implements Serializable {

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
	
	@OneToOne(fetch=FetchType.EAGER, cascade=CascadeType.DETACH)
	@JoinColumn(name="product_id", nullable=false)
	private Product product;
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="order_id", nullable=false, updatable=false)
	private Order order;
	
	@Column(name = "count")
	private int count;
	
	@Column(name = "total")
	private double total;

	@Type(type = "true_false")
	@Column(name = "deleted", nullable = false)
	private boolean deleted;
	
	@Column(name = "deleted_at")
	private Date deletedAt;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", updatable = false, nullable = false)
	private Date createdAt;

	public LineItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double getTotal() {
		if(this.product!=null) {
			this.total = this.product.getPrice() * this.count;
		}
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
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
	
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.product).toHashCode();

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
		LineItem other = (LineItem) obj;
		return new EqualsBuilder()
				.append(this.product, other.product).isEquals();
	}
	
	@PrePersist
	private void preCreate() {
		log.debug("preCreateUpdate...");
		this.uid = RandomGeneratorUtils.getLineItemUid();
		if(this.product!=null) {
			this.total = MathUtils.getTwoDecimalPlaces(this.product.getPrice() * this.count);
		}
		log.debug("lineItem={}",ObjectUtils.toJson(this));
	}
	@PreUpdate
	private void preUpdate() {
		log.debug("preCreateUpdate...");
		if(this.product!=null) {
			this.total = MathUtils.getTwoDecimalPlaces(this.product.getPrice() * this.count);
		}
		log.debug("lineItem={}",ObjectUtils.toJson(this));
	}

}

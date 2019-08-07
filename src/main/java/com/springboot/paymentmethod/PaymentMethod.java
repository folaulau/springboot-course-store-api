package com.springboot.paymentmethod;

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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.InvalidMappingException;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springboot.error.ApiError;
import com.springboot.error.ApiException;
import com.springboot.user.User;
import com.springboot.utils.MathUtils;
import com.springboot.utils.ObjectUtils;

@Where(clause = "deleted = 'F'")
@Entity
@Table(name = "payment_method")
public class PaymentMethod implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@JsonIgnore
	@Transient
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "type", updatable = false, nullable = false)
	private String type;

	@Column(name = "uid", updatable = false, nullable = false)
	private String uid;

	@Column(name = "name")
	private String name;

	@Column(name = "last4")
	private String last4;

	@Column(name = "position")
	private Integer position;

	@Column(name = "brand")
	private String brand;

	@Column(name = "source_token")
	private String sourceToken;
	
	@Column(name = "payment_gateway_id")
	private String paymentGatewayId;

	@JsonIgnore
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", updatable = false)
	private Date createdAt;

	@JsonIgnore
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at", updatable = true)
	private Date updatedAt;
	
	@Type(type = "true_false")
	@Column(name = "deleted", nullable = false)
	private boolean deleted;
    
    @ManyToOne(cascade=CascadeType.DETACH, fetch=FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false, updatable=false)
    private User user;

	public PaymentMethod() {
		super();
		// TODO Auto-generated constructor stub
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

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
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

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getSourceToken() {
		return sourceToken;
	}

	public void setSourceToken(String sourceToken) {
		this.sourceToken = sourceToken;
	}

	public String getPaymentGatewayId() {
		return paymentGatewayId;
	}

	public void setPaymentGatewayId(String paymentGatewayId) {
		this.paymentGatewayId = paymentGatewayId;
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

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
    
    
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.id).append(this.uid).append(this.user).toHashCode();
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
		PaymentMethod other = (PaymentMethod) obj;
		return new EqualsBuilder().append(this.id, other.id)
				.append(this.uid, other.uid).append(this.user, other.user).isEquals();
	}
	
	@PrePersist
	@PreUpdate
	private void preCreateUpdate() {
		log.debug("preCreateUpdate...");
		if(this.type==null) {
			throw new ApiException(new ApiError(HttpStatus.BAD_REQUEST, "type is null"));
			//throw new InvalidMappingException("type-type is null", "path-type shouldn't be empty");
		}
	}

}

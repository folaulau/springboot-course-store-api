package com.springboot.product;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.springboot.product.size.TShirtSize;
import com.springboot.utils.ApiSessionUtils;

/**
 * 
 * @author folaukaveinga
 *
 */
@JsonInclude(value = Include.NON_NULL)
@Where(clause = "deleted = 'F'")
@Entity
@Table(name = "product")
public class Product implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false, unique = true)
	private Long id;

	@Column(name = "uid", unique = true, nullable = false, updatable = false)
	private String uid;

	@Column(name = "name")
	private String name;

	@ElementCollection
	private Set<String> pages;

	// monomono, basket, etc
	@Column(name = "type")
	private String type;

	// where within type
	@Column(name = "category")
	private String category;

	// image of the product
	@Column(name = "image_url")
	private String imageUrl;

	// possible sizes
	@ElementCollection
	private Set<String> sizes;

	// star rating
	@Column(name = "rating")
	private Integer rating;

	@Column(name = "price")
	private Double price;

	// names of people whose products are on the site
	@Column(name = "vendor")
	private String vendor;

	@Column(name = "description", length = 2500)
	private String description;

	@Type(type = "true_false")
	@Column(name = "active", nullable = false)
	private Boolean active;

	@Type(type = "true_false")
	@Column(name = "deleted", nullable = false)
	private boolean deleted;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", updatable = false, nullable = false)
	private Date createdAt;
	
	@Column(name = "created_by", updatable = false, nullable = false)
	private Long createdBy;

	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at", updatable = true, nullable = false)
	private Date updatedAt;

	public Product() {
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Set<String> getPages() {
		return pages;
	}

	public void setPages(Set<String> pages) {
		this.pages = pages;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	

	public Set<String> getSizes() {
		return sizes;
	}

	public void setSizes(Set<String> sizes) {
		if(sizes!=null && sizes.size()>0) {
			sizes.forEach((size)->{
				this.addSize(size);
			});
		}else {
			this.sizes = sizes;
		}
	}
	
	public void addSizes(List<String> sizes) {
		if(sizes!=null && sizes.size()>0) {
			sizes.forEach((size)->{
				this.addSize(size);
			});
		}
	}
	
	public void addSize(String size) {
		if(this.sizes == null){
			this.sizes = new HashSet<>();
		}
		if(size!=null && TShirtSize.isValid(size)) {
			this.sizes.add(size);
		}
	}
	
	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.id).append(this.uid).append(this.name).toHashCode();
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
		Product other = (Product) obj;
		return new EqualsBuilder().append(this.id, other.id).append(this.uid, other.uid).append(this.name, other.name)
				.isEquals();
	}

	@PrePersist
	private void prePersist() {
		if (this.active == null) {
			this.active = true;
		}
		
		long userId = ApiSessionUtils.getUserId();
		
		if(userId==0) {
			this.createdBy = new Long(1);
		}else {
			this.createdBy = userId;
		}
	}

}

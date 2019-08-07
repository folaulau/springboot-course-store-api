package com.springboot.dto;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.springboot.product.size.MonomonoSize;
import com.springboot.utils.MathUtils;

@JsonInclude(value = Include.NON_NULL)
public class ProductReadDTO {

	private Long id;

	private String uid;

	private String name;

	private String imageUrl;

	private Set<String> sizes;

	private String sizeAsString;

	private Set<String> pages;

	private String type;

	private String category;

	private Integer rating;

	private Double price;
	
	private String priceAsString;

	private String vendor;

	private String description;

	private Boolean active;

	private boolean deleted;

	private Date createdAt;

	private Date updatedAt;

	public ProductReadDTO() {
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

	public Double getPrice() {
		return price;
	}
	
	public String getPriceAsString() {
		if(price!=null) {
			this.priceAsString = MathUtils.getTwoDecimalPlacesAsString(price);
		}
		return priceAsString;
	}

	public void setPrice(Double price) {
		this.price = price;
		
		if(price!=null) {
			this.priceAsString = MathUtils.getTwoDecimalPlacesAsString(price);
		}
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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
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

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Set<String> getSizes() {
		return sizes;
	}

	public void setSizes(Set<String> sizes) {
		this.sizes = sizes;

		if (this.sizes != null && this.sizes.size() > 0) {
			StringBuilder strSize = new StringBuilder();

			if (sizes.contains(MonomonoSize.CRIB.getTitle())) {
				strSize.append(", " + MonomonoSize.CRIB.getTitle());
			}
			if (sizes.contains(MonomonoSize.TWIN.getTitle())) {
				strSize.append(", " + MonomonoSize.TWIN.getTitle());
			}
			if (sizes.contains(MonomonoSize.FULL.getTitle())) {
				strSize.append(", " + MonomonoSize.FULL.getTitle());
			}
			if (sizes.contains(MonomonoSize.QUEEN.getTitle())) {
				strSize.append(", " + MonomonoSize.QUEEN.getTitle());
			}
			if (sizes.contains(MonomonoSize.KING.getTitle())) {
				strSize.append(", " + MonomonoSize.KING.getTitle());
			}
			
			if (sizes.contains(MonomonoSize.CALI_KING.getTitle())) {
				strSize.append(", CALI KING");
			}
			this.sizeAsString = strSize.toString().substring(2);
		}
	}

	public String getSizeAsString() {
		return sizeAsString;
	}

	public void setSizeAsString(String sizeAsString) {
		this.sizeAsString = sizeAsString;
	}

	public void setPriceAsString(String priceAsString) {
		this.priceAsString = priceAsString;
	}

}

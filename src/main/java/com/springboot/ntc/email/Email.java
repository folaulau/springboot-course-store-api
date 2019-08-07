package com.springboot.ntc.email;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.springboot.utils.ObjectUtils;

public class Email implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String message;
	
	private String subject;
	
	private String title;
	
	private String status;
	
	private String sendTo;
	
	private List<String> carbonCopies;
	
	private List<String> blindCopies;
	
	private List<String> attachmentUrls;
	
	private Date createdAt;

	private Date updatedAt;
	
	public Email() {
		this(null,null,null,null,null,null);
	}
	
	public Email(List<String> carbonCopies, List<String> blindCopies, List<String> attachmentUrls,
			String message, String subject, String status) {
		super();
		this.message = message;
		this.subject = subject;
		this.status = status;
		this.carbonCopies = carbonCopies;
		this.attachmentUrls = attachmentUrls;
		this.blindCopies = blindCopies;
	}

	public List<String> getCarbonCopies() {
		return carbonCopies;
	}

	public void setCarbonCopies(List<String> carbonCopies) {
		this.carbonCopies = carbonCopies;
	}
	
	public void addCarbonCopy(String carbonCopy) {
		if(this.carbonCopies==null) {
			this.carbonCopies = new ArrayList<>();
		}
		this.carbonCopies.add(carbonCopy);
	}

	public List<String> getBlindCopies() {
		return blindCopies;
	}

	public void setBlindCopies(List<String> blindCopies) {
		this.blindCopies = blindCopies;
	}
	
	public void addBlindCopy(String blindCopy) {
		if(this.blindCopies==null) {
			this.blindCopies = new ArrayList<>();
		}
		this.blindCopies.add(blindCopy);
	}

	public List<String> getAttachmentUrls() {
		return attachmentUrls;
	}

	public void setAttachmentUrls(List<String> attachmentUrls) {
		this.attachmentUrls = attachmentUrls;
	}
	
	public void addAttachmentUrl(String attachmentUrl) {
		if(this.attachmentUrls==null) {
			this.attachmentUrls = new ArrayList<>();
		}
		this.attachmentUrls.add(attachmentUrl);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getSendTo() {
		return sendTo;
	}

	public void setSendTo(String sendTo) {
		this.sendTo = sendTo;
	}

	public static Email fromJson(String json) {
		try {
			return ObjectUtils.getObjectMapper().readValue(json, Email.class);
		} catch (Exception e) {
			System.out.println("Email - fromJson - Exception, msg: "+e.getLocalizedMessage());
			return null;
		}
		
	}
	
	public String toJson() {
		try {
			return ObjectUtils.getObjectMapper().writeValueAsString(this);
		} catch (Exception e) {
			System.out.println("Email - toJson - Exception, msg: "+e.getLocalizedMessage());
			return null;
		}
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object other) {
		return EqualsBuilder.reflectionEquals(this, other);
	}

}

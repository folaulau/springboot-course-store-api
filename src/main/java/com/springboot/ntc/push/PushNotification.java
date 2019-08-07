package com.springboot.ntc.push;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
public class PushNotification implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String title;
	
	private String description;
	
	private String action;
	
	private Map<String, String> data;
	
	private Set<String> tokens;
	
	public PushNotification() {
		
	}

	public String getTitle() {
		return title;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setTitle(String title) {
		this.title = title;
	}




	public String getDescription() {
		return description;
	}




	public void setDescription(String description) {
		this.description = description;
	}




	



	public Map<String, String> getData() {
		return data;
	}

	
	public void setData(Map<String, String> data) {
		this.data = data;
	}
	
	public void addData(String key,String data) {
		if(this.data==null){
			this.data = new HashMap<>();
		}
		this.data.put(key, data);
	}

	public Set<String> getTokens() {
		return tokens;
	}




	public void setTokens(Set<String> tokens) {
		this.tokens = tokens;
	}
	
	public void addToken(String token) {
		if(this.tokens == null){
			this.tokens = new HashSet<>();
		}
		this.tokens.add(token);
	}
	
	public List<String> generateTokensAsList() {
		return new ArrayList<>(this.tokens);
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

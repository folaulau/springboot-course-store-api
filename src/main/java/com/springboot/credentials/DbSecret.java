package com.springboot.credentials;

import java.io.IOException;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springboot.utility.ObjectUtils;

public class DbSecret {
	private String username;

	private String password;
	
	private String engine;
	
	private String host;
	
	private String dbInstanceIdentifier;
	

	public DbSecret() {
		this(null,null,null,null,null);
	}

	public DbSecret(String username, String password, String engine, String host,
			String dbInstanceIdentifier) {
		super();
		this.username = username;
		this.password = password;
		this.engine = engine;
		this.host = host;
		this.dbInstanceIdentifier = dbInstanceIdentifier;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEngine() {
		return engine;
	}

	public void setEngine(String engine) {
		this.engine = engine;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getDbInstanceIdentifier() {
		return dbInstanceIdentifier;
	}

	public void setDbInstanceIdentifier(String dbInstanceIdentifier) {
		this.dbInstanceIdentifier = dbInstanceIdentifier;
	}
	
	public String toJson() {
		try {
			return ObjectUtils.getObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			System.out.println("JsonProcessingException, msg: " + e.getLocalizedMessage());
			return "{}";
		}
	}
	
	public static DbSecret fromJson(String json) {
		
		try {
			return ObjectUtils.getObjectMapper().readValue(json, DbSecret.class);
		} catch (IOException e) {
			System.out.println("From Json Exception: "+e.getLocalizedMessage());
			return null;
		}
	}
}

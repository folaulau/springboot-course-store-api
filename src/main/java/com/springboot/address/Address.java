package com.springboot.address;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@Entity
@Table(name = "address")
public class Address implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false, unique = true)
	private Long id;
	
	@Column(name = "street")
	private String street;

	@Column(name = "street2")
	private String street2;

	@Column(name = "city")
	private String city;
	
	@Column(name = "state")
	private String state;

	@Column(name = "zipcode")
	private String zip;
	
	@Column(name = "time_zone")
	private String timeZone;
	
	@Column(name = "country")
	private String country;
	
	public Address() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getStreet2() {
		return street2;
	}

	public void setStreet2(String street2) {
		this.street2 = street2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	
	
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	
	public Map<String,Object> getPaymentGatewayAddress(){
		Map<String,Object> add = new HashMap<>();
		if(street!=null && street.length()>0) {
			add.put("line1", street);
		}else {
			return null;
		}
		
		if(street2!=null && street2.length()>0) {
			add.put("line2", street2);
		}
		
		if(city!=null && city.length()>0) {
			add.put("city", city);
		}
		
		if(country!=null && country.length()>0) {
			add.put("country", country);
		}
		
		if(zip!=null && zip.length()>0) {
			add.put("postal_code", zip);
		}
		
		if(state!=null && state.length()>0) {
			add.put("state", state);
		}
		
		return add;
	}
}


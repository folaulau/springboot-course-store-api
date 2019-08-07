package com.springboot.dto;

import java.io.Serializable;
import java.util.Date;

import com.springboot.validator.Email;
import com.springboot.validator.NotEmptyString;
import com.springboot.validator.Password;

public class UserUpdateDTO  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@NotEmptyString
	private String uid;
	
	private String password;

	@Email
	private String email;
	
	private String firstName;
	
	private String lastName;
	
	private String phoneNumber;
	
	private Date dob;
	
	private AddressDTO address;

	public UserUpdateDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public AddressDTO getAddress() {
		return address;
	}

	public void setAddress(AddressDTO address) {
		this.address = address;
	}
}

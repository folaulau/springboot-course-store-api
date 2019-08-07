package com.springboot.dto;

import java.util.Date;

import com.springboot.validator.Age;
import com.springboot.validator.Email;
import com.springboot.validator.Password;

public class UserCreateDTO {

	@Email
	private String email;

	@Password
	private String password;

	private String phoneNumber;
	
	private String firstName;

	private String lastName;

	public UserCreateDTO() {
		super();
		// TODO Auto-generated constructor stub
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}

package com.springboot.user;

import java.util.Arrays;

public class UserType {

	public static final String CUSTOMER = "customer";
	public static final String ADMIN = "admin";
	public static final String SYSTEM = "system";
	
	public static boolean isValidType(String type) {
		if(type!=null && Arrays.asList(CUSTOMER,ADMIN,SYSTEM).contains(type)) {
			return true;
		}
		return false;
	}
}

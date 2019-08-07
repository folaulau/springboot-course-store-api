package com.springboot.role;

import org.apache.commons.lang3.ArrayUtils;

public interface RoleType {

	// for spring security
	public static final String USER = "USER";
	public static final String ADMIN = "ADMIN";
	public static final String MANAGER = "MANAGER";
	public static final String SYSTEM = "SYSTEM";
	
	// for application
	public static final String ROLE_USER = "ROLE_USER";
	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	public static final String ROLE_MANAGER = "ROLE_MANAGER";
	public static final String ROLE_SYSTEM = "ROLE_SYSTEM";
	
	public static boolean isValidRole(String role) {
		if(role!=null && role.length()>0) {
			if(ArrayUtils.contains(new String[] {ROLE_USER,ROLE_ADMIN,ROLE_MANAGER,ROLE_SYSTEM}, role)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isValidRole(Role role) {
		if(role!=null && role.getAuthority()!=null && role.getAuthority().length()>0) {
			if(ArrayUtils.contains(new String[] {ROLE_USER,ROLE_ADMIN,ROLE_MANAGER,ROLE_SYSTEM}, role.getAuthority())) {
				return true;
			}
		}
		return false;
	}
}

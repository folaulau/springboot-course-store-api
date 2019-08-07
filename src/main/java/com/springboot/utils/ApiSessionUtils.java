package com.springboot.utils;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.springboot.jwt.JwtPayload;
import com.springboot.jwt.JwtTokenUtils;
import com.springboot.user.User;

public class ApiSessionUtils {
	
	private static Logger log = LoggerFactory.getLogger(ApiSessionUtils.class);

	/**
	 * get jwtPayload from UsernamePasswordAuthenticationToken principal
	 * @param jwtPayload
	 */
	public static JwtPayload getApiSession() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			JwtPayload session = (JwtPayload) auth.getPrincipal();
			return session;
		}
		return null;
	}
	
	public static long getUserId() {
		JwtPayload jwt = getApiSession();
		return (jwt==null || jwt.getId()==null) ? 1L : jwt.getId();
		
	}
	
	public static String getUserUid() {
		JwtPayload jwt = getApiSession();
		return jwt.getUid();
	}
	
	public static User getUser() {
		JwtPayload jwt = getApiSession();
		User user = null;
		if(jwt!=null) {
			user = new User();
			user.setId(jwt.getId());
			user.setUid(jwt.getUid());
			user.setEmail(jwt.getEmail());
			user.setPaymentGatewayId(jwt.getPgi());
		}
		
		return user;
	}
	
	public static User getUser(Long id) {
		User user = new User();
		user.setId(id);
		return user;
	}

	/**
	 * set jwtPayload in UsernamePasswordAuthenticationToken principal
	 * @param jwtPayload
	 */
	public static void setRequestSecurityAuthentication(JwtPayload jwtPayload) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		if (jwtPayload.getAuthorities() != null || jwtPayload.getAuthorities().isEmpty() == false) {
			for (String role : jwtPayload.getAuthorities()) {
				authorities.add(new SimpleGrantedAuthority(role.toUpperCase()));
			}
		}

		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken(jwtPayload, jwtPayload.getUid(), authorities));
	}
	
	public static void setRequestSecurityAuthentication(String token) {
		JwtPayload jwtPayload = JwtTokenUtils.validateToken(token);

		if (jwtPayload == null) {
			log.warn("jwtPayload is null. UsernamePasswordAuthenticationToken is not set.");
			return;
		}
		
		setRequestSecurityAuthentication(jwtPayload);
	}
	
	public static Long getCreateBy() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			JwtPayload session = (JwtPayload) auth.getPrincipal();
			
			if(session!=null && session.getId()!=null) {
				return session.getId();
			}
			
		}
		return new Long(1);
	}
	

}

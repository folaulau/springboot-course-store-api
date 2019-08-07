package com.springboot.security;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.springboot.jwt.JwtPayload;
import com.springboot.jwt.JwtTokenUtils;
import com.springboot.utils.ApiSessionUtils;
import com.springboot.utils.ObjectUtils;

public class CustomAuthenticationFilter extends OncePerRequestFilter {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * Handle token missing error <br>
	 * Handle cached user not found error <br>
	 * Handle user with no roles <br>
	 * If all goes well, let request continue down the line
	 * 
	 * @author fkaveinga
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		log.debug("doFilterInternal(...)");
		String token = request.getHeader("token");
		log.debug("Token: {}", token);
//		log.debug("Request URL: {}", request.getRequestURL().toString());
//		log.debug("Request URI: {}", request.getRequestURI().toString());
//		log.debug("Request CONTEXT PATH: {}", request.getContextPath());
//		log.debug("Request METHOD: {}", request.getMethod());
		
		if (token == null || token.length()==0) {
			
			ObjectNode errorMsg = ObjectUtils.getObjectNode();

			errorMsg.put("status", "error");
			errorMsg.put("msg", "token is missing");
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			ObjectUtils.getObjectMapper().writeValue(response.getWriter(), errorMsg);
			
			log.debug("errorMsg={}",ObjectUtils.toJson(errorMsg));
			return;
		}

		JwtPayload jwtPayload = JwtTokenUtils.validateToken(token);

		if (jwtPayload == null) {
			
			ObjectNode erroMsg = ObjectUtils.getObjectNode();

			erroMsg.put("status", "error");
			erroMsg.put("msg", "token is invalid");
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			ObjectUtils.getObjectMapper().writeValue(response.getWriter(), erroMsg);
			return;
		}

		//log.debug("jwtPayload: {}", ObjectUtils.toJson(jwtPayload));
		
		ApiSessionUtils.setRequestSecurityAuthentication(jwtPayload);
		
		log.debug("request authenticated!");
		filterChain.doFilter(request, response);
	}
}

package com.springboot.security;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.springboot.role.Role;

@Service
public class AccessService {
	private Logger log = LoggerFactory.getLogger(this.getClass());
}

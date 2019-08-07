package com.springboot.user.admin;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.springboot.dto.EntityMapper;
import com.springboot.dto.ProductReadDTO;
import com.springboot.dto.SessionDTO;
import com.springboot.dto.UserCreateDTO;
import com.springboot.dto.UserReadDTO;
import com.springboot.error.ApiError;
import com.springboot.error.ApiErrorMessage;
import com.springboot.error.ApiException;
import com.springboot.role.Role;
import com.springboot.role.RoleType;
import com.springboot.user.User;
import com.springboot.user.UserService;
import com.springboot.user.UserType;
import com.springboot.utils.CustomPage;
import com.springboot.utils.ObjectUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Secured(value= {RoleType.ROLE_MANAGER, RoleType.ROLE_ADMIN})
@Api(value = "admin-users",produces = "Rest API for admin-user operations", tags = "Admin User Controller")
@RequestMapping("/admin/users")
@RestController
public class AdminUserController {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UserService userService;
	
	
	@Autowired
	private EntityMapper entityMapper;
	
	/**
	 * 
	 * @param apiKey
	 * @param user
	 * @return
	 */
	
	@ApiOperation(value = "Sign Up Another Admin")
	@PostMapping
	public ResponseEntity<SessionDTO> createAdminUser(@RequestHeader(name="token", required=true) String token, @ApiParam(name="user", required=true, value="user") @Valid @RequestBody UserCreateDTO userCreateDTO){
		log.debug("createAdminUser(..)");
		SessionDTO userSession = userService.signUpAdministrator(userCreateDTO);
		log.debug("userSession: {}",ObjectUtils.toJson(userSession));
		return new ResponseEntity<>(userSession, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Get Users By Type")
	@GetMapping
	public ResponseEntity<CustomPage<UserReadDTO>> getUsers(@RequestHeader(name="token", required=true) String token, 
			@ApiParam(name="type", required=true, value="type of users [customer,admin]") @RequestParam("type") String type,
			Pageable pageable,
			@ApiParam(name = "page", required = false, value = "page", defaultValue = "0") @RequestParam(required = false, name = "page", defaultValue = "0") Integer page,
			@ApiParam(name = "size", required = false, value = "size", defaultValue = "10") @RequestParam(required = false, name = "size", defaultValue = "20") Integer size,
			@ApiParam(name = "sort", required = false, value = "Sorting. format -> sort=attribute:direction&... direction values[asc,desc] i.e /search?sort=type:desc&sort=... ") @RequestParam(required = false, name = "sort") String[] sorts){
		log.debug("getUsers(..)");
		
		log.debug("page: {}, size: {}", page, size);
		
		if(UserType.isValidType(type)==false) {
			throw new ApiException(new ApiError(HttpStatus.BAD_REQUEST, ApiErrorMessage.DEFAULT_MESSAGE, "Invalid user type, type="+type));
		}
		
		Page<User> userPage = this.userService.getPage(type, pageable);
		
		CustomPage<UserReadDTO> result = new CustomPage<UserReadDTO>(userPage);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Search Users")
	@GetMapping("/search")
	public ResponseEntity<CustomPage<UserReadDTO>> searchUsersByType(@RequestHeader(name="token", required=true) String token, 
			@ApiParam(name="type", required=true, value="type of users [customer,user]") @RequestParam("type") String type,
			Pageable pageable,
			@ApiParam(name = "page", required = false, value = "page", defaultValue = "0") @RequestParam(required = false, name = "page", defaultValue = "0") Integer page,
			@ApiParam(name = "size", required = false, value = "size", defaultValue = "10") @RequestParam(required = false, name = "size", defaultValue = "20") Integer size,
			@ApiParam(name = "sort", required = false, value = "Sorting. format -> sort=attribute:direction&... direction values[asc,desc] i.e /search?sort=type:desc&sort=... ") @RequestParam(required = false, name = "sort") String[] sorts){
		log.debug("createAdminUser(..)");
		
		if(UserType.isValidType(type)) {
			throw new ApiException(
					new ApiError(HttpStatus.BAD_REQUEST, ApiErrorMessage.DEFAULT_MESSAGE, "Invalid user type, type="+type));
		}
		
		
		
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Get User By Uid")
	@GetMapping("/{uid}")
	public ResponseEntity<UserReadDTO> getUserByUid(@RequestHeader(name="token", required=true) String token, 
			@ApiParam(name="uid", required=true, value="user uid") @PathVariable("uid") String uid){
		log.debug("getUserByUid(..)");
		
		User user = this.userService.getByUid(uid);
		
		UserReadDTO userReadDTO = this.entityMapper.mapUserToUserDTO(user);
		
		return new ResponseEntity<>(userReadDTO, HttpStatus.OK);
	}

}

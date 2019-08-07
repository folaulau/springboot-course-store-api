package com.springboot.user;

import java.io.IOException;
import java.util.Arrays;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.springboot.dto.EntityMapper;
import com.springboot.dto.FileUploadStatus;
import com.springboot.dto.SessionDTO;
import com.springboot.dto.UserCreateDTO;
import com.springboot.dto.UserReadDTO;
import com.springboot.dto.UserUpdateDTO;
import com.springboot.error.ApiError;
import com.springboot.error.ApiErrorMessage;
import com.springboot.error.ApiException;
import com.springboot.error.ApiSubError;
import com.springboot.jwt.JwtPayload;
import com.springboot.utils.ApiSessionUtils;
import com.springboot.utils.FileUtils;
import com.springboot.utils.HttpUtils;
import com.springboot.utils.ObjectUtils;
import com.springboot.utils.PasswordUtils;
import com.springboot.utils.ValidationUtils;
import com.springboot.validator.PasswordValidator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "users", produces = "Rest API for user operations", tags = "User Controller")
@RequestMapping("/users")
@RestController
public class UserController {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserService userService;

	@Value("${x-api-key}")
	private String xApiKey;

	@Autowired
	private EntityMapper entityMapper;

	/**
	 * 
	 * @param apiKey
	 * @param user
	 * @return
	 */

	@ApiOperation(value = "Sign Up")
	@PostMapping("/signup")
	public ResponseEntity<SessionDTO> signUp(@RequestHeader(name = "x-api-key", required = true) String xApiKey,
			@ApiParam(name = "user", required = true, value = "user") @Valid @RequestBody UserCreateDTO signupRequest) {
		log.debug("signUp(..)");

		if (this.xApiKey.equals(xApiKey) == false) {
			throw new ApiException(
					new ApiError(HttpStatus.BAD_REQUEST, ApiErrorMessage.DEFAULT_MESSAGE, "invalid x-api-key"));
		}

		log.debug("signupRequest: {}", ObjectUtils.toJson(signupRequest));
		SessionDTO userSession = userService.signUpCustomer(signupRequest);
		log.debug("userSession: {}", ObjectUtils.toJson(userSession));
		return new ResponseEntity<>(userSession, HttpStatus.OK);
	}

	@ApiOperation(value = "Update profile")
	@PutMapping("/profile")
	public ResponseEntity<UserReadDTO> updateUser(@RequestHeader(name = "token", required = true) String token,
			@ApiParam(name = "user", required = true, value = "user") @Valid @RequestBody UserUpdateDTO userUpdateDTO) {
		log.debug("updateUser(..)={}", ObjectUtils.toJson(userUpdateDTO));

		if (ApiSessionUtils.getUserUid().equals(userUpdateDTO.getUid()) == false) {
			throw new ApiException(new ApiError(HttpStatus.BAD_REQUEST, ApiErrorMessage.DEFAULT_MESSAGE,
					"uid does not match uid for this user"));
		}

		User user = userService.getByUid(userUpdateDTO.getUid());

		if (user == null) {
			throw new ApiException(
					new ApiError(HttpStatus.BAD_REQUEST, "User not found for uid=" + userUpdateDTO.getUid()));
		}
		
		//log.debug("user={}",ObjectUtils.toJson(user));
		
		//log.debug("userUpdateDTO={}",ObjectUtils.toJson(userUpdateDTO));

		user = entityMapper.patchUpdateUser(userUpdateDTO, user);

		if (userUpdateDTO.getPassword() != null && userUpdateDTO.getPassword().length()>0) {
			if (ValidationUtils.isValidPassword(userUpdateDTO.getPassword())) {
				user.setPassword(PasswordUtils.hashPassword(userUpdateDTO.getPassword()));
			}else {
				throw new ApiException(
						new ApiError(HttpStatus.BAD_REQUEST,"Invalid password","", Arrays.asList(new ApiSubError("user","","",""))));
			}
		}
		
		//log.debug("user={}",ObjectUtils.toJson(user));

		User updatedUser = this.userService.update(user);

		return new ResponseEntity<>(entityMapper.mapUserToUserDTO(updatedUser), HttpStatus.OK);
	}

	/**
	 * This method is for show only. It does not get called on login. Check
	 * CustomLoginFilter.java - Spring security set this up.
	 * 
	 * @param apiKey
	 * @param user
	 * @return
	 */

	@ApiOperation(value = "Login")
	@PostMapping("/login")
	public ResponseEntity<SessionDTO> login(@RequestHeader(name = "x-api-key", required = true) String xApiKey,
			@ApiParam(name = "authorization", required = true, value = "Base64 username and password encoded token") @RequestHeader("authorization") String authorization) {
		log.info("login(...)");
		return new ResponseEntity<>(new SessionDTO(), HttpStatus.OK);
	}

	@ApiOperation(value = "Logout")
	@DeleteMapping("/logout")
	public ResponseEntity<?> logout(@RequestHeader("token") String token) {
		log.info("logout(...)");

		ObjectNode result = ObjectUtils.getObjectNode();
		result.put("status", "good");

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@ApiOperation(value = "Get Member By Uuid")
	@GetMapping("/{uid}")
	public ResponseEntity<UserReadDTO> getUserByUid(@RequestHeader(name = "token", required = true) String token,
			@ApiParam(name = "uid", required = true, value = "uid") @PathVariable("uid") String uid) {
		log.debug("getUserByUid(..)");

		JwtPayload jwtPayload = ApiSessionUtils.getApiSession();

		//log.debug("jwtPayload: {}", ObjectUtils.toJson(jwtPayload));

		User user = userService.getByUid(uid);

		if (user == null) {
			throw new ApiException(new ApiError(HttpStatus.BAD_REQUEST, "User not found for uid=" + uid));
		}

		UserReadDTO userReadDTO = entityMapper.mapUserToUserDTO(user);

		log.debug("userReadDTO: {}", ObjectUtils.toJson(userReadDTO));

		return new ResponseEntity<>(userReadDTO, HttpStatus.OK);
	}

	@ApiOperation(value = "Upload Profile Image")
	@PostMapping(consumes = "multipart/form-data", value = "/image")
	public ResponseEntity<UserReadDTO> uploadProfileImage(@RequestHeader(name = "token", required = true) String token,
			@ApiParam(name = "image", required = true, value = "image") @RequestPart(value = "file", required = true) MultipartFile file) {

		String imageUrl = null;

		User user = null;

		try {
			Long userId = ApiSessionUtils.getUserId();
			imageUrl = this.userService.uploadProfilePicture(userId, FileUtils.convertMultipartFileToFile(file));

			user = this.userService.updateProfileImageUrl(userId, imageUrl);

		} catch (IOException e) {
			log.error("IOException, msg={}", e.getLocalizedMessage());
			e.printStackTrace();
			throw new ApiException(new ApiError(HttpStatus.BAD_REQUEST, "Image could not be uploaded", e.getMessage()));
		}

		UserReadDTO userReadDTO = this.entityMapper.mapUserToUserDTO(user);

		return new ResponseEntity<>(userReadDTO, HttpStatus.OK);
	}
}

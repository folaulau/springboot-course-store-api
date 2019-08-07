package com.springboot.user;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NestedRuntimeException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import com.springboot.aws.s3.AwsS3Folder;
import com.springboot.aws.s3.AwsS3Service;
import com.springboot.dto.EntityMapper;
import com.springboot.dto.SessionDTO;
import com.springboot.dto.UserCreateDTO;
import com.springboot.error.ApiError;
import com.springboot.error.ApiException;
import com.springboot.jwt.JwtPayload;
import com.springboot.jwt.JwtTokenUtils;
import com.springboot.paymentgateway.PaymentGatewayService;
import com.springboot.role.Role;
import com.springboot.role.RoleType;
import com.springboot.utils.HttpUtils;
import com.springboot.utils.ObjectUtils;
import com.springboot.utils.PasswordUtils;
import com.springboot.utils.RandomGeneratorUtils;
import com.springboot.utils.SqlUtils;

@Service
public class UserServiceImp implements UserService {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private PaymentGatewayService paymentGatewayService;

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private AwsS3Service awsS3Service;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private EntityMapper entityMapper;

	@Override
	public User create(User user) {
		user.setId(new Long(0));
		user.setUid(RandomGeneratorUtils.getUserUid());
		User savedUser = null;
		try {
			savedUser = userRepository.saveAndFlush(user);
			
		} catch (NestedRuntimeException e) {
			log.warn("e.getLocalizedMessage()={}",e.getLocalizedMessage());
			log.warn("e.getCause().getMessage()={}",e.getCause().getMessage());
			log.warn("e.getCause().getLocalizedMessage()={}",e.getCause().getLocalizedMessage());
			log.warn("e.getMostSpecificCause().getMessage()={}",e.getMostSpecificCause().getMessage());
			log.warn("e.getMostSpecificCause().getLocalizedMessage()={}",e.getMostSpecificCause().getLocalizedMessage());
			
			String sqlError = SqlUtils.extractSqlError(e.getMostSpecificCause().getMessage());
			log.warn("sqlError={}",sqlError);
			
			throw new ApiException(new ApiError(HttpStatus.BAD_REQUEST, sqlError));
			
		} catch (Exception e) {
			throw new ApiException(new ApiError(HttpStatus.BAD_REQUEST, e.getMessage()));
		}
		return savedUser;
	}
	
	@Override
	public User update(User user) {
		if(user.getUid()==null) {
			return this.create(user);
		}
		return userRepository.saveAndFlush(user);
	}

	@Override
	public User getById(Long id) {
		// TODO Auto-generated method stub

		return userRepository.getById(id);
	}

	@Override
	public Optional<User> findByUid(String uuid) {
		// TODO Auto-generated method stub
		return userRepository.findByUid(uuid);
	}

	@Override
	public User getByUid(String uid) {
		log.debug("getByUid({})",uid);
		return userRepository.getByUid(uid);
	}

	@Override
	public User getProfileById(Long id) {

		User user = userRepository.getById(id);

		log.info("user: {}", ObjectUtils.toJson(user));

		return user;
	}

	@Override
	public Optional<User> findByEmail(String email) {
		// TODO Auto-generated method stub
		return this.userRepository.findByEmail(email);
	}

	@Override
	public User getByEmail(String email) {
		// TODO Auto-generated method stub
		return this.userRepository.getByEmail(email);
	}

	@Override
	public SessionDTO signUpCustomer(UserCreateDTO userCreateDTO) {
		log.debug("signup(..)");
		User user = this.entityMapper.mapUserCreateDTOToUser(userCreateDTO);
		user.setType(UserType.CUSTOMER);
		user.addRole(RoleType.ROLE_USER);

		user.setPassword(PasswordUtils.hashPassword(user.getPassword()));
		user.setUid(RandomGeneratorUtils.getUserUid());
		log.debug("user: {}", ObjectUtils.toJson(user));

		user = this.create(user);
		Map<String,Object> metadata = new HashMap<>();
		com.stripe.model.Customer customer = paymentGatewayService.create(user, metadata);
		user.setPaymentGatewayId(customer.getId());
		
		this.userRepository.setPaymentGatewayId(user.getId(), customer.getId());
		
		log.debug("saved user: {}", ObjectUtils.toJson(user));
		JwtPayload jwtpayload = new JwtPayload(user, RandomGeneratorUtils.getJwtUid());

		String clientUserAgent = HttpUtils.getRequestUserAgent(request);

		jwtpayload.setDeviceId(clientUserAgent);
		
		log.debug("jwtpayload: {}", ObjectUtils.toJson(jwtpayload));
		
		String jwtToken = JwtTokenUtils.generateToken(jwtpayload);

		SessionDTO ssnDto = this.entityMapper.mapUserAndTokenToSessionDTO(user, jwtToken);
		
		return ssnDto;
	}

	@Secured(value= {RoleType.ADMIN})
	@Override
	public SessionDTO signUpAdministrator(UserCreateDTO userCreateDTO) {
		log.debug("signUpAdministrator(..)");
		User user = this.entityMapper.mapUserCreateDTOToUser(userCreateDTO);
		user.setType(UserType.ADMIN);
		user.addRole(RoleType.ROLE_USER);
		user.addRole(RoleType.ROLE_MANAGER);
		user.addRole(RoleType.ROLE_ADMIN);

		user.setPassword(PasswordUtils.hashPassword(user.getPassword()));
		user.setUid(RandomGeneratorUtils.getUserUid());
		log.debug("user: {}", ObjectUtils.toJson(user));

		user = this.create(user);

		log.debug("saved user: {}", ObjectUtils.toJson(user));
		JwtPayload jwtpayload = new JwtPayload(user, RandomGeneratorUtils.getJwtUid());

		String clientUserAgent = HttpUtils.getRequestUserAgent(request);

		jwtpayload.setDeviceId(clientUserAgent);
		
		log.debug("jwtpayload: {}", ObjectUtils.toJson(jwtpayload));
		
		String jwtToken = JwtTokenUtils.generateToken(jwtpayload);

		SessionDTO ssnDto = new SessionDTO();
		ssnDto.setEmail(user.getEmail());
		ssnDto.setUserUid(user.getUid());
		ssnDto.setToken(jwtToken);
		return ssnDto;
	}

	@Override
	public String uploadProfilePicture(long userId, File file) {
		String key = AwsS3Folder.USER_PROFILE_IMG_TSHIRT+"/"+userId+"/"+RandomGeneratorUtils.getS3FileKey(file.getName());
		return awsS3Service.uploadFile(key, file);
	}

	@Override
	public Page<User> getPage(String type, Pageable pageable) {
		// TODO Auto-generated method stub
		return userRepository.findByType(type, pageable);
	}

	@Override
	public User updateProfileImageUrl(Long userId, String imageUrl) {
		log.debug("updateProfileImageUrl(..)");
		User user = this.getById(userId);
		if(user!=null) {
			user.setProfileImgUrl(imageUrl);
			user = this.update(user);
		}
		return user;
	}
}

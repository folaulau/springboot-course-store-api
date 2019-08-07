package com.springboot.user;

import java.io.File;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.springboot.dto.SessionDTO;
import com.springboot.dto.UserCreateDTO;

public interface UserService {
	
	User create(User user);
	
	User update(User user);
	
	User getById(Long id);
	
	User getProfileById(Long id);
	
	User getByUid(String uid);
	
	Optional<User> findByUid(String uid);

	Optional<User> findByEmail(String email);
	
	User getByEmail(String email);

	SessionDTO signUpCustomer(UserCreateDTO signupRequest);
	
	SessionDTO signUpAdministrator(UserCreateDTO signupRequest);
	
	String uploadProfilePicture(long userId, File file);
	
	Page<User> getPage(String type, Pageable pageable);

	User updateProfileImageUrl(Long userId, String imageUrl);
}

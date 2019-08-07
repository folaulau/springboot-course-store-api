package com.springboot.loader;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.springboot.address.Address;
import com.springboot.paymentgateway.PaymentGatewayService;
import com.springboot.paymentmethod.PaymentMethod;
import com.springboot.paymentmethod.PaymentMethodService;
import com.springboot.paymentmethod.PaymentMethodType;
import com.springboot.role.Role;
import com.springboot.role.RoleType;
import com.springboot.user.User;
import com.springboot.user.UserRepository;
import com.springboot.user.UserService;
import com.springboot.user.UserType;
import com.springboot.utils.DataGeneratorUtils;
import com.springboot.utils.ObjectUtils;
import com.springboot.utils.PasswordUtils;
import com.springboot.utils.RandomGeneratorUtils;

@Component
public class UserLoader {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserService userService;

	@Autowired
	private PaymentGatewayService paymentGatewayService;

	@Autowired
	private UserRepository userRepository;

	@PostConstruct
	public void init() {
		 loadSystemUser();
		
		 loadAdmins();
		
		 loadUsers();
	}

	public void loadSystemUser() {
		log.info("loadSystemUser...");

		List<User> adminUsers = userRepository.findByType(UserType.SYSTEM);

		if (adminUsers != null && adminUsers.size() > 0) {
			log.info("system users already loaded!");
			return;
		}

		User user = new User();
		user.setUid(RandomGeneratorUtils.getUserUid());
		user.setEmail("folaudev+tshirt_system_user@gmail.com");
		user.setPassword(PasswordUtils.hashPassword("Test1234!"));
		user.setType(UserType.SYSTEM);
		user.addRole(RoleType.ROLE_SYSTEM);

		user = userService.create(user);

	}

	public void loadAdmins() {
		log.info("loadAdmins...");

		List<User> adminUsers = userRepository.findByType(UserType.ADMIN);

		if (adminUsers != null && adminUsers.size() > 0) {
			log.info("admin users already loaded!");
			return;
		}

		User user = new User();
		user.setFirstName("Folau");
		user.setLastName("Kaveinga");
		user.setType(UserType.ADMIN);
		user.setDob(DateUtils.setMonths(DateUtils.setYears(new Date(), 1986), 11));
		user.setUid(RandomGeneratorUtils.getUserUid());
		user.setPhoneNumber("3101234567");
		user.setProfileImgUrl("https://raw.githubusercontent.com/folaulau/pub_resources/master/img/profile.jpeg");
		user.setEmail("folaudev@gmail.com");
		user.setPassword(PasswordUtils.hashPassword("Test1234!"));

		Address address = new Address();
		address.setCity("Lenox");
		address.setState("CA");
		address.setStreet("4849 w 111th st");
		address.setZip("90204");

		user.setAddress(address);

		user.addRole(RoleType.ROLE_USER);
		user.addRole(RoleType.ROLE_MANAGER);
		user.addRole(RoleType.ROLE_ADMIN);

		user = userService.create(user);

	}

	public void loadUsers() {
		log.info("loadUsers...");
		List<User> users = new ArrayList<>();

		List<User> adminUsers = userRepository.findByType(UserType.CUSTOMER);

		if (adminUsers != null && adminUsers.size() > 0) {
			log.info("customer users already loaded!");
			return;
		}

		for (int i = 1; i < 10; i++) {
			User user = new User();
			user.setPhoneNumber("310" + RandomGeneratorUtils.getIntegerWithin(1000000, 9999999));
			user.setUid(RandomGeneratorUtils.getUserUid());
			user.setFirstName(DataGeneratorUtils.getRandomFirstName());
			user.setLastName(DataGeneratorUtils.getRandomLastName());
			user.setEmail("folaudev+tshirt" + i + "@gmail.com");
			user.setPassword(PasswordUtils.hashPassword("Test1234!"));
			user.setType(UserType.CUSTOMER);

			Address address = new Address();
			address.setCity("El Segundo");
			address.setState("CA");
			address.setStreet("Rosescrans");
			address.setZip("90305");

			user.setAddress(address);

			user.addRole(RoleType.ROLE_USER);

			user = this.userService.create(user);

			Map<String, Object> metadata = new HashMap<>();
			com.stripe.model.Customer customer = paymentGatewayService.create(user, metadata);
			user.setPaymentGatewayId(customer.getId());

			this.userRepository.setPaymentGatewayId(user.getId(), customer.getId());

			// users.add(user);
		}

		// this.userRepository.saveAll(users);
	}
}

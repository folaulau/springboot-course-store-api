package com.springboot.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUid(String uuid);
	
	List<User> findByType(String type);

	User getById(Long id);

	@Query("select u from User u where u.uid = :uid")
	User getByUid(@Param("uid") String uid);

	Optional<User> findByEmail(String email);

	User getByEmail(String email);

	Page<User> findByType(String type, Pageable pageable);

	@Async
	@Modifying
	@Transactional
	@Query("update User user set user.paymentGatewayId = :paymentGatewayId where user.id = :id")
	void setPaymentGatewayId(@Param("id") Long id, @Param("paymentGatewayId") String paymentGatewayId);

}

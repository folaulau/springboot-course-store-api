package com.springboot.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.product.Product;

import java.lang.String;

public interface ProductRepository extends JpaRepository<Product, Long> {

	Product findByUid(String uid);
	
	Page<Product> findByActive(Boolean active, Pageable page);
}

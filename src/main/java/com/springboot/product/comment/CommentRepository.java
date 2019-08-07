package com.springboot.product.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.product.comment.Comment;

import java.lang.String;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	
	Comment findByUid(String uid);
	
	Page<Comment> findByProductId(Long productId, Pageable pageable);
	
	Page<Comment> findByProductUid(String productUid, Pageable pageable);
	
}

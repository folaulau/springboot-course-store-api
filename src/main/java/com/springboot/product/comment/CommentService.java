package com.springboot.product.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {

	Comment create(Comment comment);
	Comment update(Comment comment);
	Comment getById(Long id);
	Comment getByUid(String uid);
	Page<Comment> getPageByProductId(Long productId, Pageable pageable);
	Page<Comment> getPageByProductUid(String productUid, Pageable pageable);
	
}

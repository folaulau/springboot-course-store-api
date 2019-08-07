package com.springboot.product.comment;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImp implements CommentService {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Override
	public Comment create(Comment comment) {
		return commentRepository.saveAndFlush(comment);
	}

	@Override
	public Comment update(Comment comment) {
		return commentRepository.saveAndFlush(comment);
	}

	@Override
	public Comment getById(Long id) {
		Optional<Comment> opComment = this.commentRepository.findById(id);
		return opComment.orElse(null);
	}

	@Override
	public Comment getByUid(String uid) {
		// TODO Auto-generated method stub
		return commentRepository.findByUid(uid);
	}

	@Override
	public Page<Comment> getPageByProductId(Long productId, Pageable pageable) {
		// TODO Auto-generated method stub
		return commentRepository.findByProductId(productId, pageable);
	}

	@Override
	public Page<Comment> getPageByProductUid(String productUid, Pageable pageable) {
		// TODO Auto-generated method stub
		return commentRepository.findByProductUid(productUid, pageable);
	}

}

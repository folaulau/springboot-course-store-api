package com.springboot.product;

import java.io.File;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.springboot.aws.s3.AwsS3Folder;
import com.springboot.aws.s3.AwsS3Service;
import com.springboot.utils.RandomGeneratorUtils;

@Service
public class ProductServiceImp implements ProductService {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private AwsS3Service awsS3Service;
	
	@Override
	public Product create(Product product) {
		log.debug("create(..)");
		if(product.getUid()==null) {
			product.setUid(RandomGeneratorUtils.geProductUid());
		}
		
		
		Product savedProduct = productRepository.saveAndFlush(product);
		
		return savedProduct;
	}

	@Override
	public Product update(Product product) {
		log.debug("update(..)");
		if(product.getUid()==null) {
			return this.create(product);
		}
		
		Product updatedProduct = productRepository.saveAndFlush(product);
		
		return updatedProduct;
	}
	
	@Override
	public List<Product> update(List<Product> products) {
		return productRepository.saveAll(products);
	}


	@Override
	public Product getById(Long id) {
		log.debug("getById({})",id);
		return this.productRepository.findById(id).orElse(null);
	}

	@Override
	public Product getByUid(String uid) {
		log.debug("getByUid({})", uid);
		return productRepository.findByUid(uid);
	}

	@Override
	public Page<Product> getByPage(Pageable page) {
		log.debug("getByPage(..)");
		return productRepository.findByActive(true, page);
	}

	@Override
	public String uploadProductProfileImg(File file) {
		String key = AwsS3Folder.PRODUCT_TSHIRT+"/"+RandomGeneratorUtils.getS3FileKey(file.getName());
		return awsS3Service.uploadFile(key, file);
	}

}

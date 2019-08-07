package com.springboot.product.admin;

import java.io.IOException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.springboot.dto.EntityMapper;
import com.springboot.dto.FileUploadStatus;
import com.springboot.dto.ProductCreateDTO;
import com.springboot.dto.ProductReadDTO;
import com.springboot.dto.ProductUpdateDTO;
import com.springboot.error.ApiError;
import com.springboot.error.ApiException;
import com.springboot.product.Product;
import com.springboot.product.ProductService;
import com.springboot.utils.FileUtils;
import com.springboot.utils.ObjectUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "products",produces = "Rest API for product operations for admin", tags = "Admin Product Controller")
@RequestMapping("/admin/products")
@RestController
public class AdminProductController {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ProductService productService;
	
	
	@Autowired
	private EntityMapper entityMapper;
	
	/**
	 * 
	 * @param apiKey
	 * @param user
	 * @return
	 */
	
	@ApiOperation(value = "Create Product")
	@PostMapping
	public ResponseEntity<ProductReadDTO> createProduct(
			@RequestHeader(name="token", required=true) String token,
			@ApiParam(name="product", required=true, value="product") @Valid @RequestBody ProductCreateDTO productCreateDTO){
		log.debug("createProduct={}", ObjectUtils.toJson(productCreateDTO));
		
		Product product = entityMapper.mapProductCreateDTOToProduct(productCreateDTO);
		
		log.debug("presave product={}", ObjectUtils.toJson(product));
		
		product = productService.create(product);
		
		log.debug("postsave product={}", ObjectUtils.toJson(product));
		
		ProductReadDTO productReadDTO = entityMapper.mapProductToProductReadDTO(product);
		
		log.debug("postsave productReadDTO={}", ObjectUtils.toJson(productReadDTO));
		
		return new ResponseEntity<>(productReadDTO, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Update Product")
	@PutMapping
	public ResponseEntity<ProductReadDTO> updateProduct(
			@RequestHeader(name="token", required=true) String token,
			@ApiParam(name="product", required=true, value="product") @Valid @RequestBody ProductUpdateDTO productUpdateDTO){
		log.debug("updateProduct={}", ObjectUtils.toJson(productUpdateDTO));
		
		if(productUpdateDTO.getUid()==null) {
			throw new ApiException(new ApiError(HttpStatus.BAD_REQUEST, "Unable to update product", "product uid is missing"));
		}
		
		Product product = productService.getByUid(productUpdateDTO.getUid());
		
		if(product==null) {
			throw new ApiException(new ApiError(HttpStatus.BAD_REQUEST, "Unable to update product", "product not found for uid="+productUpdateDTO.getUid()));
		}
		
		product = entityMapper.patchUpdateProduct(productUpdateDTO, product);
		
		product = productService.update(product);
		
		ProductReadDTO productReadDTO = entityMapper.mapProductToProductReadDTO(product);
		
		log.debug("productReadDTO={}",ObjectUtils.toJson(productReadDTO));
		
		
		return new ResponseEntity<>(productReadDTO, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Upload Product Profile Image")
	@PostMapping(value="/image", consumes="multipart/form-data")
	public ResponseEntity<FileUploadStatus> uploadProfileImage(
			@RequestHeader(name="token", required=true) String token,
			@ApiParam(name = "image", required = true, value = "image") @RequestPart(value = "file", required = true) MultipartFile file) {
		
		String imageUrl = null;
		try {
			imageUrl = productService.uploadProductProfileImg(FileUtils.convertMultipartFileToFile(file));
		} catch (IOException e) {
			log.error("IOException, msg={}",e.getLocalizedMessage());
			e.printStackTrace();
			throw new ApiException(new ApiError(HttpStatus.BAD_REQUEST, "Image could not be uploaded", e.getMessage()));
		}
		
		FileUploadStatus status = new FileUploadStatus();
		
		if(imageUrl!=null) {
			status.setUrl(imageUrl);
			status.setUploaded(true);
		}else {
			status.setUploaded(false);
		}
		
		return new ResponseEntity<>(status, HttpStatus.OK);
	}
}

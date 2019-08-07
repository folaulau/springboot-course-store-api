package com.springboot.product;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.springboot.dto.EntityMapper;
import com.springboot.dto.FileUploadStatus;
import com.springboot.dto.ProductCreateDTO;
import com.springboot.dto.ProductReadDTO;
import com.springboot.dto.ProductUpdateDTO;
import com.springboot.dto.SessionDTO;
import com.springboot.dto.UserCreateDTO;
import com.springboot.error.ApiError;
import com.springboot.error.ApiException;
import com.springboot.jwt.JwtPayload;
import com.springboot.role.Role;
import com.springboot.utils.ApiSessionUtils;
import com.springboot.utils.CustomPage;
import com.springboot.utils.FileUtils;
import com.springboot.utils.HttpUtils;
import com.springboot.utils.ObjectUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "products",produces = "Rest API for product operations", tags = "Product Controller")
@RequestMapping("/products")
@RestController
public class ProductController {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private EntityMapper entityMapper;
	
	@ApiOperation(value = "Get Product By Uuid")
	@GetMapping("/{uid}")
	public ResponseEntity<ProductReadDTO> getProductByUid( @ApiParam(name="uid", required=true, value="uid") @PathVariable("uid") String uid){
		log.debug("getProductByUid({})", uid);
		
		Product product = productService.getByUid(uid);
		
		if(product==null) {
			throw new ApiException(new ApiError(HttpStatus.BAD_REQUEST, "Product not found", "Product not found for uid="+uid));
		}
		
		ProductReadDTO productReadDTO = entityMapper.mapProductToProductReadDTO(product);
		
		log.debug("productReadDTO: {}",ObjectUtils.toJson(productReadDTO));
		
		return new ResponseEntity<>(productReadDTO, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Get Products")
	@GetMapping
	public ResponseEntity<CustomPage<ProductReadDTO>> getProducts(
			Pageable pageable,
			@ApiParam(name = "page", required = false, value = "page", defaultValue = "0") @RequestParam(required = false, name = "page", defaultValue = "0") Integer page,
			@ApiParam(name = "size", required = false, value = "size", defaultValue = "10") @RequestParam(required = false, name = "size", defaultValue = "20") Integer size,
			@ApiParam(name = "sort", required = false, value = "Sorting. format -> sort=attribute:direction&... direction values[asc,desc] i.e /search?sort=type:desc&sort=... ") @RequestParam(required = false, name = "sort") String[] sorts,
			@RequestParam(required = false, name = "types") List<String> types){
		log.debug("getProducts({})");
		
		//log.debug("pageable: {}",ObjectUtils.toJson(pageable));
		
		Page<Product> resultPage = this.productService.getByPage(pageable);
		
//		List<Product> products = result.getContent();
//		
//		log.debug("products: {}",ObjectUtils.toJson(products));
		
		CustomPage<ProductReadDTO> result = new CustomPage<ProductReadDTO>(resultPage);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
}

package com.springboot.loader;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.springboot.address.Address;
import com.springboot.product.Product;
import com.springboot.product.ProductService;
import com.springboot.product.ProductType;
import com.springboot.product.size.MonomonoSize;
import com.springboot.role.Role;
import com.springboot.user.User;
import com.springboot.user.UserService;
import com.springboot.utils.MathUtils;
import com.springboot.utils.ObjectUtils;
import com.springboot.utils.PasswordUtils;
import com.springboot.utils.RandomGeneratorUtils;

@Component
public class ProductLoader {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ProductService productService;
	
	@PostConstruct
	public void init() throws InterruptedException {
		loadProducts();
	}
	
	
	public void loadProducts() throws InterruptedException {
		log.info("loadProducts...");
		
		Page<Product> page = productService.getByPage(new PageRequest(0, 5));
		
		
		if(page.isEmpty()==false) {
			log.info("products already loaded!");
			return;
		}
		
		for(int i=0;i<25;i++) {
			Product smallMonomono = new Product();
			smallMonomono.setName("Kafu Putu");
			smallMonomono.setDescription("Kafu used for a putu");
			smallMonomono.setPrice(MathUtils.getTwoDecimalPlaces(RandomGeneratorUtils.getDoubleWithin(100, 300)));
			smallMonomono.setRating(RandomGeneratorUtils.getIntegerWithin(2, 6));
			smallMonomono.setType(ProductType.MONOMONO);
			smallMonomono.setVendor("MOM");
			smallMonomono.addSizes(Arrays.asList(MonomonoSize.CRIB.getTitle(),MonomonoSize.TWIN.getTitle(),MonomonoSize.FULL.getTitle(),MonomonoSize.QUEEN.getTitle(),MonomonoSize.KING.getTitle(),MonomonoSize.CALI_KING.getTitle()));
			smallMonomono.setImageUrl("https://s3-us-west-2.amazonaws.com/dev-resource-100/monomono/blanket"+RandomGeneratorUtils.getIntegerWithin(1, 11)+".jpg");
			smallMonomono = productService.create(smallMonomono);
		}
		
	}
}

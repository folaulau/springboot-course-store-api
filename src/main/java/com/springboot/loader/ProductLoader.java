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
import com.springboot.product.size.TShirtSize;
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
		
		Page<Product> page = productService.getByPage(new PageRequest(0, 5));
		
		
		if(page.isEmpty()==false) {
			log.info("products already loaded!");
			return;
		}
		
		loadProducts();
		
		loadProducts();
	}
	
	
	public void loadProducts() throws InterruptedException {
		log.info("loadProducts...");
		
		for(int i=0;i<24;i++) {
			
			Product smallMonomono = new Product();
			
			smallMonomono.setName("T Shirt "+(i+1));
			smallMonomono.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Morbi tristique senectus et netus et.");
			smallMonomono.setPrice(MathUtils.getTwoDecimalPlaces(RandomGeneratorUtils.getDoubleWithin(5, 25)));
			smallMonomono.setRating(RandomGeneratorUtils.getIntegerWithin(2, 6));
			smallMonomono.setType(ProductType.TSHIRT);
			smallMonomono.setVendor("Lisa");
			smallMonomono.addSizes(Arrays.asList(TShirtSize.SMALL.getTitle(),TShirtSize.MEDIUM.getTitle(),TShirtSize.LARGE.getTitle(),TShirtSize.XLARGE.getTitle(),TShirtSize.X2LARGE.getTitle(),TShirtSize.X3LARGE.getTitle(),TShirtSize.X4LARGE.getTitle(),TShirtSize.X5LARGE.getTitle()));
			
			smallMonomono.setImageUrl("https://s3-us-west-2.amazonaws.com/dev-resource-100/springboot-course-tshirt/shirt/tshirt-"+(i+1)+".jpeg");
			
			smallMonomono = productService.create(smallMonomono);
		}
		
	}
}

package com.springboot.order.admin;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.springboot.dto.EntityMapper;
import com.springboot.dto.OrderAdminSearchResponseItemDTO;
import com.springboot.dto.OrderReadDTO;
import com.springboot.error.ApiError;
import com.springboot.error.ApiException;
import com.springboot.order.Order;
import com.springboot.order.OrderService;
import com.springboot.order.lineitem.LineItemService;
import com.springboot.product.ProductService;
import com.springboot.utils.CustomPage;
import com.springboot.utils.search.AdminOrderSearchFilter;
import com.springboot.utils.search.Sorting;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "orders", produces = "Rest API for order operations", tags = "Admin Order Controller")
@RestController
@RequestMapping("/admin/orders")
public class AdminOrderController {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private OrderService oderService;
	
	@Autowired
	private LineItemService lineItemService;
	
	
	@Autowired
	private ProductService productService;

	@Autowired
	private EntityMapper entityMapper;

	@ApiOperation(value = "Get Order By Uuid")
	@GetMapping("/{uid}")
	public ResponseEntity<OrderReadDTO> getOrderByUid(
			@RequestHeader(name = "token", required = false) String token,
			@ApiParam(name = "uid", required = true, value = "uid") @PathVariable("uid") String uid) {
		log.debug("getOrderByUid({})", uid);
		
		Order order = this.oderService.getByUid(uid);

		if (order == null) {
			throw new ApiException(
					new ApiError(HttpStatus.BAD_REQUEST, "Order not found", "Order not found for uid=" + uid));
		}
		
		if (order.getCustomer() != null && (token==null || token.length()==0)) {
			throw new ApiException(
					new ApiError(HttpStatus.BAD_REQUEST, "Order not found", "You can't access this order"));
		}
		
		if (order.getCustomer() != null && (token==null || token.length()==0)) {
			throw new ApiException(
					new ApiError(HttpStatus.BAD_REQUEST, "Order not found", "You can't access this order"));
		}

		OrderReadDTO orderReadDTO = this.entityMapper.mapOrderToOrderReadDTO(order);

		return new ResponseEntity<>(orderReadDTO, HttpStatus.OK);
	}
	

	@ApiOperation(value = "Get Orders")
	@GetMapping
	public ResponseEntity<CustomPage<OrderReadDTO>> getOrders(@RequestHeader(name="token", required=true) String token,
			Pageable pageable,
			@ApiParam(name = "page", required = false, value = "page", defaultValue = "0") @RequestParam(required = false, name = "page", defaultValue = "0") Integer page,
			@ApiParam(name = "size", required = false, value = "size", defaultValue = "10") @RequestParam(required = false, name = "size", defaultValue = "20") Integer size,
			@ApiParam(name = "sort", required = false, value = "Sorting. format -> sort=attribute:direction&... direction values[asc,desc] i.e /search?sort=type:desc&sort=... ") @RequestParam(required = false, name = "sort") String[] sorts) {
		log.debug("getOrders({})");
		
		Page<Order> orderPage = this.oderService.getPage(pageable);
		
		CustomPage<OrderReadDTO> result = new CustomPage<OrderReadDTO>(orderPage);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Search Orders")
	@GetMapping("/search")
	public ResponseEntity<CustomPage<OrderAdminSearchResponseItemDTO>> search(@RequestHeader(name="token", required=true) String token,
			Pageable pageable,
			@ApiParam(name = "page", required = false, value = "page", defaultValue = "0") @RequestParam(required = false, name = "page", defaultValue = "0") Integer page,
			@ApiParam(name = "size", required = false, value = "size", defaultValue = "10") @RequestParam(required = false, name = "size", defaultValue = "20") Integer size,
			@ApiParam(name = "sort", required = false, value = "Sorting. format -> sort=attribute:direction&... direction values[asc,desc] i.e /search?sort=type:desc&sort=... ") @RequestParam(required = false, name = "sort") String[] sorts,
			
			@RequestParam(required = false, name = "amounts") List<Integer> amounts,
			@RequestParam(required = false, name = "paid") Boolean paid,
			@RequestParam(required = false, name = "delivered") Boolean delivered,
			@RequestParam(required = false, name = "query") String query) {
		log.debug("getOrders({})");
		
		List<Sorting> sortings = null;
		if (sorts != null && sorts.length > 0) {
			sortings = AdminOrderSearchFilter.validateSortBy(sorts);
		}
		
		if (amounts != null && amounts.isEmpty() == false && amounts.size() > 0) {
			AdminOrderSearchFilter.validateAmounts(amounts);
		}
		
		Page<OrderAdminSearchResponseItemDTO> orderPage = this.oderService.search(pageable,amounts,query,sortings);
		
		CustomPage<OrderAdminSearchResponseItemDTO> result = new CustomPage<OrderAdminSearchResponseItemDTO>(orderPage);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
}

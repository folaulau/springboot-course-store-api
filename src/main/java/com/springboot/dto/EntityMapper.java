package com.springboot.dto;

import java.util.List;

import javax.validation.Valid;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;

import com.springboot.address.Address;
import com.springboot.order.Order;
import com.springboot.order.lineitem.LineItem;
import com.springboot.order.paymentmethod.OrderPaymentMethod;
import com.springboot.payment.Payment;
import com.springboot.paymentmethod.PaymentMethod;
import com.springboot.product.Product;
import com.springboot.user.User;
import com.stripe.model.Charge;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface EntityMapper {
	
	//========================= User =====================
	
	User mapUserDtoToUser(UserUpdateDTO dto);

	UserReadDTO mapUserToUserDTO(User user);

	User mapUserCreateDTOToUser(UserCreateDTO dto);
	
	User mapUserUpdateDTOToUser(UserUpdateDTO dto);
	
	@Mappings({ 
		@Mapping(target = "id", ignore = true), 
		@Mapping(target = "uid", ignore = true) 
	})
	User patchUpdateUser(UserUpdateDTO userUpdateDTO, @MappingTarget User user);
	
	//========================= Product =====================
	
	Product mapProductCreateDTOToProduct(ProductCreateDTO productCreateDTO); 
	
	Product mapProductUpdateDTOToProduct(ProductUpdateDTO productUpdateDTO); 
	
	ProductReadDTO mapProductToProductReadDTO(Product product); 
	
	List<ProductReadDTO> mapProductsToProductReadDTOs(List<Product> product);
	
	@Mappings({ 
		@Mapping(target = "id", ignore = true), 
		@Mapping(target = "uid", ignore = true) 
	})
	Product patchUpdateProduct(ProductUpdateDTO productUpdateDTO, @MappingTarget Product product);
	
	//========================= Payment Method =====================
	
	PaymentMethod mapCardPMCreateDTOToPaymentMethod(CardPMCreateDTO cardPMCreateDTO);
	
	List<PaymentMethodReadDTO> mapPaymentMethodsToPaymentMethodReadDTOs(List<PaymentMethod> paymentMethods);
	
	//========================= Order =====================
	
	Order mapOrderCreateDTOToOrder(OrderCreateDTO orderCreateDTO);
	
	OrderReadDTO mapOrderToOrderReadDTO(Order order);
	
	OrderPaymentMethod mapPaymentMethodToOrderPaymentMethod(PaymentMethod paymentMethod);
	
	@Mappings(value= {
			@Mapping(target = "token", source = "token"),
			@Mapping(target = "userUid", source = "user.uid")
	})
	SessionDTO mapUserAndTokenToSessionDTO(User user, String token);
	
	LineItemDTO mapLineItemToLineItemDTO(LineItem lineItem);

	LineItem mapLineItemDTOToLineItem(LineItemDTO lineItemDTO);

	Address mapAddressDTOToAddress(AddressDTO addressDTO);

	

}

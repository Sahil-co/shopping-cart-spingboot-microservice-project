package com.programmingtechie.orderservcie.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.programmingtechie.orderservcie.dto.OrderLineItemsDto;
import com.programmingtechie.orderservcie.dto.OrderRequest;
import com.programmingtechie.orderservcie.model.Order;
import com.programmingtechie.orderservcie.model.OrderLineItems;
import com.programmingtechie.orderservcie.repository.OrderRepository;

@Service
@Transactional
public class OrderService {

	@Autowired
	private OrderRepository orderRepository; 
	
	public void placeOrder(OrderRequest orderRequest) {
		Order order = new Order();
		order.setOrderNumber(UUID.randomUUID().toString());
		
	    List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
	    		.stream()
			    .map(orderLineItemsDto -> mapToDto(orderLineItemsDto))
			    .toList();
		
		order.setOrderLineItemsList(orderLineItems);
		
		//Call Inventory Service and place order if product is in Stock
		
		
		
		orderRepository.save(order);
	}
	
	private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
		OrderLineItems orderLineItems = new OrderLineItems();
		orderLineItems.setPrice(orderLineItemsDto.getPrice());
		orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
		orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
		return orderLineItems;
	}
}

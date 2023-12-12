package com.programmingtechie.orderservcie.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import com.programmingtechie.orderservcie.dto.InventoryResponse;
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
	
	@Autowired
	private WebClient.Builder webClientBuilder;
	
	public void placeOrder(OrderRequest orderRequest) {
		Order order = new Order();
		order.setOrderNumber(UUID.randomUUID().toString());
		
	    List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
	    		.stream()
			    .map(orderLineItemsDto -> mapToDto(orderLineItemsDto))
			    .toList();
		
		order.setOrderLineItemsList(orderLineItems);
		
		List<String> skuCodes = order.getOrderLineItemsList().stream()
								.map(orderLineItem -> orderLineItem.getSkuCode())
								.toList();
		
		//Call Inventory Service and place order if product is in Stock
		InventoryResponse inventoryResponse[] = webClientBuilder.build().get()
				 .uri("http://inventory-service/api/inventory", 
						 uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
				 .retrieve()
				 .bodyToMono(InventoryResponse[].class)
				 .block();
		
		Boolean allProductsInStock =  Arrays.stream(inventoryResponse)
				.allMatch(inventoryResponses -> inventoryResponses.isInStock());
		
		if(allProductsInStock) {
			orderRepository.save(order);
		}
		else {
			throw new IllegalArgumentException("Product is not is stock, please try again later");
		}
		
	}
	
	private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
		OrderLineItems orderLineItems = new OrderLineItems();
		orderLineItems.setPrice(orderLineItemsDto.getPrice());
		orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
		orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
		return orderLineItems;
	}
}

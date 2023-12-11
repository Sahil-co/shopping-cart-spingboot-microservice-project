package com.programmingtechie.orderservcie.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

	private List<OrderLineItemsDto> orderLineItemsDtoList;
}

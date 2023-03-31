package com.delivery.order.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemsRequest {

    private Integer id;
    private Double price;
    private Integer quantity;
    private Integer restaurantId;
    private String name;

}

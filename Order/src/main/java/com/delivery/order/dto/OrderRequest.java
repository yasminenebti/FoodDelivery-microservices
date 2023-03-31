package com.delivery.order.dto;

import com.delivery.order.entity.OrderStatus;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private OrderStatus orderStatus ;

    private double total;

    private List<OrderItemsRequest> orderItemsRequests = new ArrayList<>(); ;


}

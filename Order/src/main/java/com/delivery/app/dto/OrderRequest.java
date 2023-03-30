package com.delivery.app.dto;

import com.delivery.app.entity.OrderItems;
import com.delivery.app.entity.OrderStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;
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

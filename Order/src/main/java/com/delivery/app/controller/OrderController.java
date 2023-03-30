package com.delivery.app.controller;

import com.delivery.app.dto.OrderRequest;
import com.delivery.app.entity.Order;
import com.delivery.app.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.*;


@RestController
@RequestMapping("api/v3/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addOrder(@RequestBody OrderRequest orderRequest){
          orderService.addOrder(orderRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Order> getOrders(){
        return orderService.getAll();
    }

    @PutMapping("/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public Order checkout(
            @PathVariable("orderId") Integer orderId,
            @RequestBody Map<String, String> requestBody
    ) {
        String address = requestBody.get("address");
        return  orderService.checkOut(orderId,address);}

}

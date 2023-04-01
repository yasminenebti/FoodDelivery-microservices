package com.delivery.order.controller;

import com.delivery.order.dto.OrderRequest;
import com.delivery.order.entity.Order;
import com.delivery.order.service.OrderService;
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

    @PutMapping("/{orderId}/increase/{item}")
    @ResponseStatus(HttpStatus.OK)
    public Order increaseItemQuantity(
            @PathVariable("orderId") Integer orderId,
            @PathVariable("item") Integer item
    ) {
        return orderService.increaseQuantity(orderId,item);
    }

    @PutMapping("/{orderId}/decrease/{item}")
    @ResponseStatus(HttpStatus.OK)
    public Order decreaseItemQuantity(
            @PathVariable("orderId") Integer orderId,
            @PathVariable("item") Integer item
    ) {
        return orderService.decreaseQuantity(orderId,item);
    }
}

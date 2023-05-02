package com.delivery.order.controller;

import com.delivery.order.dto.OrderItemsRequest;
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
    public void addOrder(@RequestBody OrderItemsRequest orderItemsRequest ,
                         @RequestHeader("x-auth-user-id") Integer userId
    ){
          orderService.addOrder(orderItemsRequest , userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Order> getOrders(){
        return orderService.getAll();
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Order checkout(
            @RequestBody Map<String, String> requestBody
    ) {
        String address = requestBody.get("address");
        return  orderService.checkOut(address);}

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

    @PutMapping("/{orderId}/deleteItem/{item}")
    @ResponseStatus(HttpStatus.OK)
    public Order deleteItemFromOrder(
            @PathVariable("orderId") Integer orderId,
            @PathVariable("item") Integer item
    ) {
        return orderService.deleteItemFromOrder(orderId,item);
    }
}

package com.delivery.app.service;

import com.delivery.app.dto.OrderItemsRequest;
import com.delivery.app.dto.OrderRequest;
import com.delivery.app.entity.Order;
import com.delivery.app.entity.OrderItems;
import com.delivery.app.entity.OrderStatus;
import com.delivery.app.food.FoodClient;
import com.delivery.app.food.FoodItemResponse;
import com.delivery.app.notification.NotificationClient;
import com.delivery.app.notification.NotificationRequest;
import com.delivery.app.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final NotificationClient notificationClient;

    private final FoodClient foodClient;

    @Autowired
    public OrderService(OrderRepository orderRepository, NotificationClient notificationClient, FoodClient foodClient) {
        this.orderRepository = orderRepository;
        this.notificationClient = notificationClient;
        this.foodClient = foodClient;
    }

    public void addOrder(OrderRequest orderRequest){
        Order order = new Order();

        List<OrderItems> orderItems = orderRequest.getOrderItemsRequests().stream()
                .map(this::mapToOrderItems)
                .toList(); //this is a lambda function replacing orderItemsRequest that maps into the function



        order.setOrderItems(orderItems);

        double total = orderItems.stream()
                .mapToDouble(this::mapToTotalOrderItems).sum();

        order.setTotal(total);

        order.setLocalDateTime(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.RECEIVED);

        orderRepository.save(order);

    }

    private double mapToTotalOrderItems(OrderItems orderItems) {
        double totalItems = 0.0;
        totalItems += orderItems.getPrice() * orderItems.getQuantity();
        return totalItems;
    }




    private OrderItems
    mapToOrderItems(OrderItemsRequest orderItemsRequest) {
        OrderItems orderItems = new OrderItems();
        FoodItemResponse foodItemResponse = foodClient.getFoodItemById(orderItemsRequest.getId());
        orderItems.setPrice(foodItemResponse.price());
        orderItems.setQuantity(1);
        orderItems.setName(foodItemResponse.name());
        return orderItems;
    }

    public Order checkOut(Integer orderId , String address){
        Order order = orderRepository.findById(orderId).orElseThrow(()-> new IllegalArgumentException("order not found"));
        order.setAddress(address);
        orderRepository.saveAndFlush(order);

        notificationClient.sendNotification(
                new NotificationRequest(
                        order.getId(),
                        String.format("New Order with Id" + orderId + "is placed successfully")
                )
        );

        return order;
    }

    public List<Order> getAll(){
        return orderRepository.findAll();
    }

}

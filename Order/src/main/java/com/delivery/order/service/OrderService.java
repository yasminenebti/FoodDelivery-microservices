package com.delivery.order.service;

import com.delivery.amqp.RabbitMqMessageProducer;
import com.delivery.order.dto.OrderItemsRequest;
import com.delivery.order.dto.OrderRequest;
import com.delivery.order.entity.Order;
import com.delivery.order.entity.OrderItems;
import com.delivery.order.entity.OrderStatus;
import com.delivery.clients.food.FoodClient;
import com.delivery.clients.food.FoodItemResponse;
import com.delivery.clients.notification.NotificationClient;
import com.delivery.clients.notification.NotificationRequest;
import com.delivery.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final NotificationClient notificationClient;

    private final FoodClient foodClient;

    private final RabbitMqMessageProducer rabbitMqMessageProducer;

    @Autowired
    public OrderService(OrderRepository orderRepository, NotificationClient notificationClient, FoodClient foodClient, RabbitMqMessageProducer rabbitMqMessageProducer) {
        this.orderRepository = orderRepository;
        this.notificationClient = notificationClient;
        this.foodClient = foodClient;
        this.rabbitMqMessageProducer = rabbitMqMessageProducer;
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

        NotificationRequest notificationRequest =
                new NotificationRequest(
                        order.getId(),
                        String.format("New Order with Id" + orderId + "is placed successfully")
                );

        rabbitMqMessageProducer.publish(
                notificationRequest,
                "internal.exchange",
                "internal.notification.routing-key"
        );


        return order;
    }

    public List<Order> getAll(){
        return orderRepository.findAll();
    }

}

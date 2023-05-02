package com.delivery.order.service;

import com.delivery.app.amqp.RabbitMqMessageProducer;
import com.delivery.order.dto.OrderItemsRequest;
import com.delivery.order.entity.Order;
import com.delivery.order.entity.OrderItems;
import com.delivery.order.entity.OrderStatus;
import com.delivery.clients.food.FoodClient;
import com.delivery.clients.food.FoodItemResponse;
import com.delivery.clients.notification.NotificationRequest;
import com.delivery.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private Order currentOrder;
    private final OrderRepository orderRepository;
    private final FoodClient foodClient;
    private final RabbitMqMessageProducer rabbitMqMessageProducer;
    @Autowired
    public OrderService(OrderRepository orderRepository, FoodClient foodClient, RabbitMqMessageProducer rabbitMqMessageProducer1) {
        this.orderRepository = orderRepository;
        this.foodClient = foodClient;
        this.rabbitMqMessageProducer = rabbitMqMessageProducer1;
        this.currentOrder=null;
    }

    public void addOrder(OrderItemsRequest orderItemsRequest , Integer userId){
        currentOrder = (currentOrder == null) ? new Order() : currentOrder;
        if (currentOrder.getOrderItems() == null) { currentOrder.setOrderItems(new ArrayList<>());}
        OrderItems orderItems = mapToOrderItems(orderItemsRequest);
        currentOrder.getOrderItems().add(orderItems);

        currentOrder.setTotal(currentOrder.getOrderItems().stream().mapToDouble(this::mapToTotalOrderItems).sum());
        currentOrder.setLocalDateTime(LocalDateTime.now());
        currentOrder.setOrderStatus(OrderStatus.RECEIVED);
        currentOrder.setUserId(userId);
        orderRepository.save(currentOrder);
    }

    private double mapToTotalOrderItems(OrderItems orderItems) {
        return orderItems.getPrice() * orderItems.getQuantity();
    }

    private Map<Integer , OrderItems> grtOrderItemsMap(Order currentOrder) {
        Map<Integer , OrderItems> map = new HashMap<>();
        for (OrderItems item : currentOrder.getOrderItems()) {
            map.put(item.getFoodId(), item);
        }
        return map;
    }

    private OrderItems mapToOrderItems(OrderItemsRequest orderItemsRequest) {
        FoodItemResponse foodItemResponse = foodClient.getFoodItemById(orderItemsRequest.getFoodId());

        Map<Integer , OrderItems> orderItemsMap = grtOrderItemsMap(currentOrder);
        if (orderItemsMap.containsKey(foodItemResponse.id())) {
            OrderItems orderItem = orderItemsMap.get(foodItemResponse.id());
            orderItem.setQuantity(orderItem.getQuantity() + 1);
            return orderItem;
        } else {
        OrderItems orderItems = new OrderItems();
        orderItems.setFoodId(foodItemResponse.id());
        orderItems.setPrice(foodItemResponse.price());
        orderItems.setQuantity(1);
        orderItems.setName(foodItemResponse.name());
        return orderItems;
    }
    }

    public Order increaseQuantity(Integer orderId , Integer orderItemId) {
        Order order = orderRepository.findById(orderId).orElseThrow(()-> new IllegalArgumentException("order not found"));
        OrderItems orderItem = order.getOrderItems().stream().filter(item -> item.getId().equals(orderItemId)).findFirst().orElseThrow();
        orderItem.setQuantity(orderItem.getQuantity()+1);
        order.setTotal(order.getOrderItems().stream().mapToDouble(this::mapToTotalOrderItems).sum());
        orderRepository.save(order);
        return order;
    }
    public Order decreaseQuantity(Integer orderId, Integer orderItemId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("order not found"));

        OrderItems orderItems = order.getOrderItems().stream()
                .filter(item -> item.getId().equals(orderItemId))
                .findFirst()
                .orElseThrow();

        if (orderItems.getQuantity() > 1) {
            orderItems.setQuantity(orderItems.getQuantity() - 1);
            order.setTotal(order.getOrderItems().stream().mapToDouble(this::mapToTotalOrderItems).sum());
            orderRepository.save(order);
        } else {
            order.getOrderItems().remove(orderItems);
            order.setTotal(order.getOrderItems().stream().mapToDouble(this::mapToTotalOrderItems).sum());
            if (order.getOrderItems().isEmpty()) {
                orderRepository.deleteById(orderId);
            } else {
                orderRepository.save(order);
            }
        }
        return order;
    }

    public Order deleteItemFromOrder(Integer orderId, Integer orderItemId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("order not found"));
        OrderItems itemToDelete = order.getOrderItems().stream().filter(item -> item.getId().equals(orderItemId) ).findFirst().orElseThrow();
        order.getOrderItems().remove(itemToDelete);
        order.setTotal(order.getOrderItems().stream().mapToDouble(this::mapToTotalOrderItems).sum());
        if (order.getOrderItems().isEmpty()) {
            orderRepository.deleteById(orderId);
        } else {
            orderRepository.save(order);
        }
        return order;

    }

    public Order checkOut(String address){
        if (currentOrder == null) { throw new IllegalStateException("no order available");}
        currentOrder.setAddress(address);
        currentOrder.setIsCheckout(true);
        orderRepository.saveAndFlush(currentOrder);

        NotificationRequest notificationRequest =
                new NotificationRequest(
                        currentOrder.getId(),
                        String.format("New Order with Id" + currentOrder.getId() + "is placed successfully")
                );

        rabbitMqMessageProducer.publish(
                notificationRequest,
                "internal.exchange",
                "internal.notification.routing-key"
        );
        Order checkoutOrder = currentOrder;
        currentOrder = null;
        return checkoutOrder;

    }


    public List<Order> getAll(){
        return orderRepository.findAll();
    }

}

package com.delivery.clients.food;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "foodItem" , url = "http://localhost:8081")
public interface FoodClient {
    @GetMapping("api/v1/food/{foodId}")
     FoodItemResponse getFoodItemById(@PathVariable("foodId") Integer foodId);
}

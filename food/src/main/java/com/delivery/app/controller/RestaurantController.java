package com.delivery.app.controller;

import com.delivery.app.entity.Category;
import com.delivery.app.entity.Restaurant;
import com.delivery.app.service.RestaurantService;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/v2/restaurant")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createRestaurant(@RequestBody Restaurant restaurant) {
        return ResponseEntity.accepted().body(restaurantService.createRestaurant(restaurant));
    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getAllRestaurants(){
        return ResponseEntity.accepted().body(restaurantService.getRestaurants());
    }

    @DeleteMapping("/{restaurantId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteRestaurant(@PathVariable("restaurantId") Integer restaurantId) {
        restaurantService.deleteRestaurant(restaurantId);
    }


    @PutMapping("/{restaurantId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateRestaurant(@PathVariable("restaurantId") Integer restaurantId,
                               @RequestBody Restaurant restaurant) {
        restaurantService.updateRestaurant(restaurantId , restaurant);
    }
}

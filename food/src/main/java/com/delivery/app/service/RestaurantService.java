package com.delivery.app.service;

import com.delivery.app.entity.Restaurant;
import com.delivery.app.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public List<Restaurant> getRestaurants() {
        return restaurantRepository.findAll();
    }
    public Restaurant createRestaurant(Restaurant restaurant){
        return restaurantRepository.save(restaurant);

    }

    public Restaurant getById(Integer id) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(id);
        if (restaurant.isEmpty()) {
            throw  new IllegalStateException("restaurant with id " + id + " does not exist");
        }
        return restaurant.get();
    }
}

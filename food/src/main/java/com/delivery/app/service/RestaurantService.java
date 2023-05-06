package com.delivery.app.service;

import com.delivery.app.entity.Category;
import com.delivery.app.entity.Restaurant;
import com.delivery.app.exception.ResourceNotFoundException;
import com.delivery.app.repository.RestaurantRepository;
import com.delivery.app.validation.ObjectValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final ObjectValidator<Restaurant> validation;

    public List<Restaurant> getRestaurants() {
        return restaurantRepository.findAll();
    }
    public Restaurant createRestaurant(Restaurant restaurant){
        validation.validate(restaurant);
        return restaurantRepository.save(restaurant);

    }

    public Restaurant getById(Integer id) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(id);
        if (restaurant.isEmpty()) {
            throw  new ResourceNotFoundException(String.format("Restaurant with ID %s not found",id));
        }
        return restaurant.get();
    }
    @Transactional
    public Restaurant updateRestaurant(Integer id , Restaurant restaurant){
        Optional<Restaurant> restauToUpdate = restaurantRepository.findById(id);
        if (restauToUpdate.isPresent()){
            validation.validate(restaurant);
            restaurant.setId(id);
            return restaurantRepository.save(restaurant);
        }
        else throw new ResourceNotFoundException(String.format("Restaurant with ID %s not found",id));
    }

    public void deleteRestaurant(Integer id){
        boolean exists = restaurantRepository.existsById(id);
        if (!exists) {
            throw  new ResourceNotFoundException(String.format("Restaurant with ID %s not found",id));
        }
        restaurantRepository.deleteById(id);
    }

}

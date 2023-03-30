package com.delivery.app.service;

import com.delivery.app.dto.FoodRequest;
import com.delivery.app.entity.Category;
import com.delivery.app.entity.FoodItem;
import com.delivery.app.entity.Restaurant;
import com.delivery.app.food.FoodItemResponse;
import com.delivery.app.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;
    private final RestaurantService restaurantService;
    private final CategoryService categoryService;
    private final FileService fileService;


    public void addFood(FoodRequest foodRequest){
        Restaurant restaurant = restaurantService.getById(foodRequest.getRestaurantId());
        Category category = categoryService.getById(foodRequest.getCategoryId());
        System.out.println(restaurant);

        FoodItem food = new FoodItem();
        food.setName(foodRequest.getName());
        food.setDescription(foodRequest.getDescription());
        food.setPrice(foodRequest.getPrice());
        food.setRestaurant(restaurant);

        food.setCategory(category);

         foodRepository.save(food);


    }

    public String addFoodImage(MultipartFile file, Integer foodId) throws IOException {
        FoodItem foodItemImage = foodRepository.findById(foodId)
                .orElseThrow(()-> new IllegalArgumentException("food not found"));

        String imageUrl = fileService.uploadFile(file);

        foodItemImage.setImage(imageUrl);

        foodRepository.save(foodItemImage);
        return imageUrl;

    }

    public List<FoodItem> getAll() {
        return foodRepository.findAll();
    }

    public FoodItemResponse getById(Integer id) {
        Optional<FoodItem> foodItem = foodRepository.findById(id);
        if (foodItem.isEmpty()) {
            throw  new IllegalStateException("foodItem with id " + id + " does not exist");
        }
        return new FoodItemResponse(foodItem.get().getName(),foodItem.get().getPrice());
    }
}

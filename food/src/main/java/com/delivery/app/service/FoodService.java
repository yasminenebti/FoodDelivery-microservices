package com.delivery.app.service;

import com.delivery.app.dto.FoodRequest;
import com.delivery.app.entity.Category;
import com.delivery.app.entity.FoodItem;
import com.delivery.app.entity.Restaurant;
import com.delivery.app.exception.ResourceNotFoundException;
import com.delivery.app.validation.ObjectValidator;
import com.delivery.clients.food.FoodItemResponse;
import com.delivery.app.repository.FoodRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Validation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FoodService {
    private final Logger logger = LoggerFactory.getLogger(FoodService.class);

    private final FoodRepository foodRepository;
    private final RestaurantService restaurantService;
    private final CategoryService categoryService;
    private final FileService fileService;
    private final ObjectValidator<Category> categoryObjectValidator;
    private final ObjectValidator<Restaurant> restaurantObjectValidator;


    public void addFood(FoodRequest foodRequest){
        Restaurant restaurant = restaurantService.getById(foodRequest.getRestaurantId());
        restaurantObjectValidator.validate(restaurant);
        Category category = categoryService.getById(foodRequest.getCategoryId());
        categoryObjectValidator.validate(category);

        FoodItem food = new FoodItem();
        food.setName(foodRequest.getName());
        food.setDescription(foodRequest.getDescription());
        food.setPrice(foodRequest.getPrice());
        food.setRestaurant(restaurant);
        food.setCategory(category);
        foodRepository.save(food);

    }

    @Transactional
    public String addFoodImage(MultipartFile file, Integer foodId) throws IOException {
        FoodItem foodItemImage = foodRepository.findById(foodId)
                .orElseThrow(()-> new ResourceNotFoundException(String.format("food with ID %s not found",foodId)));

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
            throw  new ResourceNotFoundException("foodItem with id " + id + " does not exist");
        }
        return new FoodItemResponse( foodItem.get().getId() , foodItem.get().getName(),foodItem.get().getPrice());
    }

    public List<FoodItem> getFoodItemByCategory(Integer categoryId) {
        logger.info("Category ID: {}", categoryId);
        Category category = categoryService.getById(categoryId);
        return foodRepository.findByCategoryName(category.getName());

    }


}

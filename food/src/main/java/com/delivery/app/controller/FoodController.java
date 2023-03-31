package com.delivery.app.controller;

import com.delivery.app.dto.FoodRequest;
import com.delivery.clients.food.FoodItemResponse;
import com.delivery.app.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/food")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addFood(@RequestBody FoodRequest foodRequest){
         foodService.addFood(foodRequest);

    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public String addImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("foodId") Integer foodId) throws IOException {
        return foodService.addFoodImage(file,foodId);
    }

    @GetMapping("/{foodId}")
    public FoodItemResponse getFoodItemById(@PathVariable("foodId") Integer foodId){
        return foodService.getById(foodId);
    }

}

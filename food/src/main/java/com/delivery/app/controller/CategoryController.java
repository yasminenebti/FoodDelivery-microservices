package com.delivery.app.controller;

import com.delivery.app.entity.Category;
import com.delivery.app.entity.Restaurant;
import com.delivery.app.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;



    @PostMapping
    public Category createCategory(@RequestBody Category category) {
        return categoryService.createCategory(category);
    }

}

package com.delivery.app.service;

import com.delivery.app.entity.Category;
import com.delivery.app.entity.Restaurant;
import com.delivery.app.repository.CategoryRepository;
import com.delivery.app.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }
    public Category createCategory(Category category){
        return categoryRepository.save(category);
    }

    public Category getById(Integer id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()) {
            throw  new IllegalStateException("category with id " + id + " does not exist");
        }
        return category.get();
    }
}

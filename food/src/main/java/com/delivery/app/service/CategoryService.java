package com.delivery.app.service;

import com.delivery.app.entity.Category;
import com.delivery.app.exception.ResourceNotFoundException;
import com.delivery.app.repository.CategoryRepository;
import com.delivery.app.validation.ObjectValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final ObjectValidator<Category> validation;
    private final CategoryRepository categoryRepository;

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }
    public Category createCategory(Category category){
        validation.validate(category);
        return categoryRepository.save(category);
    }

    public Category getById(Integer id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()) {
            throw  new ResourceNotFoundException(String.format("Category with ID %s not found",id));
        }
        return category.get();
    }

    @Transactional
    public Category updateCategory(Integer id , Category category){
        Optional<Category> categoryToUpdate = categoryRepository.findById(id);
        if (categoryToUpdate.isPresent()){
            validation.validate(category);
            category.setId(id);
            return categoryRepository.save(category);
        }
        else throw new ResourceNotFoundException(String.format("Category with ID %s not found",id));
    }

    public void deleteCategory(Integer id){
        boolean exists = categoryRepository.existsById(id);
        if (!exists) {
            throw  new ResourceNotFoundException(String.format("Category with ID %s not found",id));
        }
        categoryRepository.deleteById(id);
    }


}

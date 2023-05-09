package com.delivery.app.repository;

import com.delivery.app.entity.Category;
import com.delivery.app.entity.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<FoodItem,Integer>{


    List<FoodItem> findByCategoryName(String name);
}
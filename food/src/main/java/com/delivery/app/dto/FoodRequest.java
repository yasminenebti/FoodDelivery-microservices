package com.delivery.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodRequest {


    private String name ;
    private String description;
    private String image;
    private Integer price;
    private Integer categoryId;
    private Integer restaurantId;

}

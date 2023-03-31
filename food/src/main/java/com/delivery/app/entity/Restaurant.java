package com.delivery.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurant")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    private String name;

    private String address;

    private Integer phoneNumber;

    @JsonIgnore
    @OneToMany(mappedBy = "restaurant" , cascade = CascadeType.ALL , fetch = FetchType.EAGER)
    private List<FoodItem> foodItemList = new ArrayList<>();
}

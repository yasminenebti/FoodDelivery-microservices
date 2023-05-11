package com.delivery.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
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

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @NotEmpty
    private String address;


    private Integer phoneNumber;

    @JsonIgnore
    @OneToMany(mappedBy = "restaurant" , cascade = CascadeType.ALL , fetch = FetchType.EAGER)
    private List<FoodItem> foodItemList = new ArrayList<>();
}

package com.helloworld.dreamshopsbackend.request;
import com.helloworld.dreamshopsbackend.model.Category;

import lombok.*;

import java.math.BigDecimal;

@Data
public class addProductRequest {
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;
    private Category category;
}

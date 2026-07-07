package com.helloworld.dreamshopsbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category   {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Product> products;


    public Category(String name) {
        this.name = name;
    }
}

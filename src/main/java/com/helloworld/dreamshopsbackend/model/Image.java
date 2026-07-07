package com.helloworld.dreamshopsbackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Blob;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String  fileName;
    private String fileType;

    @Lob
    private Blob image;


    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;
}

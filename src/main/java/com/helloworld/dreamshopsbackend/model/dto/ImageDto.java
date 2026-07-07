package com.helloworld.dreamshopsbackend.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ImageDto {

    private Long imageId;
    private String imageName;
    private String fileType;
    private String downloadUrl;
}

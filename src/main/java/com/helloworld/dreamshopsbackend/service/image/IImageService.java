package com.helloworld.dreamshopsbackend.service.image;

import com.helloworld.dreamshopsbackend.model.dto.ImageDto;
import com.helloworld.dreamshopsbackend.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {

    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDto> saveImages(List<MultipartFile> file, Long productId);
    void updateImageById(MultipartFile file, Long imageId);
}

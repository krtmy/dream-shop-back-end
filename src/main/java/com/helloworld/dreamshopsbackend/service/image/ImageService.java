package com.helloworld.dreamshopsbackend.service.image;

import com.helloworld.dreamshopsbackend.model.dto.ImageDto;
import com.helloworld.dreamshopsbackend.exception.category.ResourceNotFoundException;
import com.helloworld.dreamshopsbackend.model.Image;
import com.helloworld.dreamshopsbackend.model.Product;
import com.helloworld.dreamshopsbackend.repository.ImageRepository;
import com.helloworld.dreamshopsbackend.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService{


    private final ImageRepository imageRepository;
    private final IProductService service;
    private final ModelMapper modelMapper;

    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Image not found with id: " + id)
        );
    }

    @Override
    public void deleteImageById(Long id) {
        if (!imageRepository.existsById(id)) {
            throw new ResourceNotFoundException("Image not found with id: " + id);
        }
        imageRepository.deleteById(id);
    }

    @Override
    public List<ImageDto> saveImages(List<MultipartFile> files, Long productId) {

        Product product = service.getProductById(productId);

        List<ImageDto> imageDto = new ArrayList<>();

        for (MultipartFile file : files) {

            try {
                Image image = new Image();

                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                Image savedImage = imageRepository.save(image);

                imageDto.add(convertImageToDto(savedImage));

            } catch (IOException | SQLException e) {
                throw new RuntimeException("Failed to save image", e);
            }
        }

        return imageDto;
    }


    @Override
    public void updateImageById(MultipartFile file, Long imageId) {

        Image image = getImageById(imageId);

        try {

            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));

            imageRepository.save(image);

        } catch (IOException | SQLException e) {
            throw new RuntimeException("Failed to update image", e);
        }
    }
    private ImageDto convertImageToDto(Image image) {

        ImageDto imageDto = modelMapper.map(image, ImageDto.class);

        imageDto.setImageId(image.getId());
        imageDto.setImageName(image.getFileName());
        imageDto.setDownloadUrl("/api/v1/images/image/download/" + image.getId());

        return imageDto;
    }
}

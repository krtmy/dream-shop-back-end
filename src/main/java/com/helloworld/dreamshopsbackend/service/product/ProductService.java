package com.helloworld.dreamshopsbackend.service.product;

import com.helloworld.dreamshopsbackend.exception.category.ResourceAlreadyExistException;
import com.helloworld.dreamshopsbackend.model.dto.ImageDto;
import com.helloworld.dreamshopsbackend.model.dto.ProductDto;
import com.helloworld.dreamshopsbackend.exception.product.ProductNotFoundException;
import com.helloworld.dreamshopsbackend.model.Category;
import com.helloworld.dreamshopsbackend.model.Image;
import com.helloworld.dreamshopsbackend.model.Product;
import com.helloworld.dreamshopsbackend.repository.CategoryRepository;
import com.helloworld.dreamshopsbackend.repository.ImageRepository;
import com.helloworld.dreamshopsbackend.repository.ProductRepository;
import com.helloworld.dreamshopsbackend.request.ProductUpdateRequest;
import com.helloworld.dreamshopsbackend.request.addProductRequest;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{


    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ModelMapper mapper;

    @Override
    public Product addProduct(addProductRequest request) {
        //check if the category is found in the DB
        //if yes, set it as the new product category,
        //if no, then save it as new category
        // and set it as the new product category
        if (productExists(request.getName(), request.getBrand())) {
            throw new ResourceAlreadyExistException(request.getName() + " " + request.getBrand() + "  already exists");
        }
        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                 .orElseGet(
                         ()-> {
                             Category newCategory = new Category(request.getCategory().getName());
                             return categoryRepository.save(newCategory);
                         }
                 );
        request.setCategory(category);
        return repository.save(createProduct(request, category));
    }


    private boolean productExists(String name, String brand) {
        return repository.existsByNameAndBrand(name, brand);
    }

    private Product createProduct(addProductRequest request, Category category) {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }


    @Override
    public Product getProductById(Long id) {
        return repository.findById(id)
                .orElseThrow(()-> new ProductNotFoundException("Product id:" + id + " not found!"));
    }

    @Override
    @Transactional
    public void deleteProductById(Long id) {
        repository.findById(id).map(
                product -> {
                    product.getImages().clear();
                    repository.saveAndFlush(product);
                    repository.delete(product);
                    return product;
                }
        ).orElseThrow(()->  new ProductNotFoundException("Product Not found!"));
    }

    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());
        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;
    }
    @Override
    public Product updateProduct(ProductUpdateRequest request, Long productId) {
        return repository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                .map(repository :: save)
                .orElseThrow(() -> new ProductNotFoundException("Product Not found!"));
    }

    @Override
    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return repository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return repository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return repository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String productName) {
        return repository.findByName(productName);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return repository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductByBrandAndName(String brand, String name) {
        return repository.countByBrandAndName(brand, name);
    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products) {
        return products.stream().map(this::convertToDto).toList();
    }

    @Override
    public ProductDto convertToDto(Product product) {
        ProductDto dto = mapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDto = images.stream().map(image -> mapper.map(image, ImageDto.class)).toList();

        dto.setImages(imageDto);

        return dto;
    }
}

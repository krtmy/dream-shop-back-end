package com.helloworld.dreamshopsbackend.service.product;

import com.helloworld.dreamshopsbackend.model.dto.ProductDto;
import com.helloworld.dreamshopsbackend.model.Product;
import com.helloworld.dreamshopsbackend.request.ProductUpdateRequest;
import com.helloworld.dreamshopsbackend.request.addProductRequest;

import java.util.List;


public interface IProductService {

    Product addProduct(addProductRequest product);
    Product getProductById(Long id);
    void deleteProductById(Long id);
    Product updateProduct(ProductUpdateRequest request, Long productId);
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String  brand);
    List<Product> getProductsByCategoryAndBrand(String category, String brand);
    List<Product> getProductsByName(String productName);
    List<Product> getProductsByBrandAndName(String brand, String name);
    Long countProductByBrandAndName(String brand, String name);

    List<ProductDto> getConvertedProducts(List<Product> products);

    ProductDto convertToDto(Product product);
}

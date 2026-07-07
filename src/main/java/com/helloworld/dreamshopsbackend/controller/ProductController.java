package com.helloworld.dreamshopsbackend.controller;

import com.helloworld.dreamshopsbackend.exception.category.ResourceAlreadyExistException;
import com.helloworld.dreamshopsbackend.model.dto.ProductDto;
import com.helloworld.dreamshopsbackend.exception.category.ResourceNotFoundException;
import com.helloworld.dreamshopsbackend.model.Product;
import com.helloworld.dreamshopsbackend.request.ProductUpdateRequest;
import com.helloworld.dreamshopsbackend.request.addProductRequest;
import com.helloworld.dreamshopsbackend.response.ApiResponse;
import com.helloworld.dreamshopsbackend.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("${product-api.prefix}")
public class ProductController {

    private final ProductService service;

    @GetMapping("/product/get-all")
    public ResponseEntity<ApiResponse> getAllProducts() {
        List<Product> products = service.getAllProducts();
        List<ProductDto> convertedProduct = service.getConvertedProducts(products);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.FOUND.toString(), convertedProduct));
    }

    @GetMapping("/product/get/{productId}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
        try {
            Product getProduct = service.getProductById(productId);
            ProductDto getDto = service.convertToDto(getProduct);
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.toString(), getDto));
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(HttpStatus.NOT_FOUND.toString(), null));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/product/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody addProductRequest product) {
        try {
            Product addProduct = service.addProduct(product);
            ProductDto addProductDto = service.convertToDto(addProduct);
            return ResponseEntity.ok(
                    new ApiResponse(
                            "Product added" , addProductDto
                    )
            );
        } catch (ResourceAlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    new ApiResponse(
                            e.getMessage(), null
                    )
            );
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/product/update/{id}")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductUpdateRequest product, @PathVariable Long id) {

        try{
            Product updatedProduct = service.updateProduct(product, id);
            ProductDto updateProductDto = service.convertToDto(updatedProduct);
            return ResponseEntity.ok(
                    new ApiResponse(
                            "Product " + id + " updated ", updateProductDto
                    )
            );
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse(
                            e.getMessage(), null
                    )
            );
        }
    }

    @DeleteMapping("/product/delete/{productId}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable  Long productId) {
        try {
            service.deleteProductById(productId);
            return ResponseEntity.ok(
                    new ApiResponse(
                            "Product: " + productId + " deleted" , null
                    )
            );
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse(
                            e.getMessage(), null
                    )
            );
        }
    }

    @GetMapping("/product/by/brand-and-name")
    public ResponseEntity<ApiResponse> getProductByBrandAndName(@RequestParam String brandName, @RequestParam String productName) {
        try {
            List<Product> products = service.getProductsByBrandAndName(brandName, productName);

            if (products.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(
                                new ApiResponse("No product found", null)
                        );
            }
            List<ProductDto> convertedProducts = service.getConvertedProducts(products);
            return ResponseEntity.ok(
                    new ApiResponse("success", convertedProducts)
            );
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponse(e.getMessage(), null)
                    );
        }
    }

    @GetMapping("/product/by/category-and-brand")
    public ResponseEntity<ApiResponse> getProductByCategoryAndBrand( @RequestParam String category,@RequestParam String brandName) {
        log.info("Brand:{} Category:{}", brandName, category);
        try {
            List<Product> products = service.getProductsByCategoryAndBrand(category, brandName);

            if (products.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(
                                new ApiResponse("No product found", null)
                        );
            }
            List<ProductDto> convertedProducts = service.getConvertedProducts(products);
            return ResponseEntity.ok(
                    new ApiResponse("success", convertedProducts)
            );
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponse(e.getMessage(), null)
                    );
        }

    }

    @GetMapping("/product/{category}/all")
    public ResponseEntity<ApiResponse> getAllProductByCategory(@PathVariable String category) {
        try {
            List<Product> products = service.getProductsByCategory(category);

            if (products.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(
                                new ApiResponse("No product found", null)
                        );
            }
            List<ProductDto> convertedProducts = service.getConvertedProducts(products);
            return ResponseEntity.ok(
                    new ApiResponse("success", convertedProducts)
            );
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponse(e.getMessage(), null)
                    );
        }

    }

    @GetMapping("/product/by-name/{name}")
    public ResponseEntity<ApiResponse> getProductByName(@PathVariable String name) {
        try {
            List<Product> products = service.getProductsByName(name);
            if (products.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(
                                new ApiResponse("No product found", null)
                        );
            }
            List<ProductDto> convertedProducts = service.getConvertedProducts(products);
            return ResponseEntity.ok(
                    new ApiResponse("success", convertedProducts)
            );
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponse(e.getMessage(), null)
                    );
        }
    }

    @GetMapping("/product/by-brand")
    public ResponseEntity<ApiResponse> getProductByBrand(@RequestParam String brand) {
        try {
            List<Product> products = service.getProductsByBrand(brand);
            if (products.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(
                                new ApiResponse("No product found", null)
                        );
            }
            List<ProductDto> convertedProducts = service.getConvertedProducts(products);
            return ResponseEntity.ok(
                    new ApiResponse("success", convertedProducts)
            );
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponse(e.getMessage(), null)
                    );
        }
    }

}

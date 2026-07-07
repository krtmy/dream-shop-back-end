package com.helloworld.dreamshopsbackend.controller;

import com.helloworld.dreamshopsbackend.exception.category.ResourceNotFoundException;
import com.helloworld.dreamshopsbackend.model.Cart;
import com.helloworld.dreamshopsbackend.response.ApiResponse;
import com.helloworld.dreamshopsbackend.service.cart.CartItemService;
import com.helloworld.dreamshopsbackend.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("${cart-api.prefix}")
@RequiredArgsConstructor
public class CartController {

    private final CartService service;
    private final CartItemService cartItemService;

    @GetMapping("/{cartId}/get-cart")
    public ResponseEntity<ApiResponse> getCart(@PathVariable Long cartId) {
        try {
            Cart cart = service.getCart(cartId);
            return ResponseEntity.ok(
                    new ApiResponse(
                            HttpStatus.FOUND.toString(),
                            cart
                    )
            );
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(
                            e.getMessage(),
                            null
                    )
            );
        }
    }

    @DeleteMapping("/{cartId}/delete")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable Long cartId) {
        try {
            service.clearCart(cartId);
            return ResponseEntity.ok(
                    new ApiResponse(
                            HttpStatus.OK.toString(),
                            null
                    )
            );
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(
                            e.getMessage(),
                            null
                    )
            );
        }

    }

    @GetMapping("/{cartId}/get-total-price")
    public ResponseEntity<ApiResponse> getTotalAmount(@PathVariable Long cartId) {

        try {
            BigDecimal totalPrice = service.getTotalPrice(cartId);
            return ResponseEntity.ok(
                    new ApiResponse(
                            HttpStatus.OK.toString(),
                            totalPrice
                    )
            );
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(
                            e.getMessage(),
                            null
                    )
            );
        }
    }


}

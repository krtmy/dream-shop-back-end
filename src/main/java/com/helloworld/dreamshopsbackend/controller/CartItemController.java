package com.helloworld.dreamshopsbackend.controller;


import com.helloworld.dreamshopsbackend.exception.category.ResourceNotFoundException;
import com.helloworld.dreamshopsbackend.model.Cart;
import com.helloworld.dreamshopsbackend.model.User;
import com.helloworld.dreamshopsbackend.response.ApiResponse;
import com.helloworld.dreamshopsbackend.service.cart.CartItemService;
import com.helloworld.dreamshopsbackend.service.cart.CartService;
import com.helloworld.dreamshopsbackend.service.user.UserService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${cartItem-api.prefix}")
@RequiredArgsConstructor
public class  CartItemController {

    private final CartItemService service;
    private final CartService cartService;
    private final UserService userService;
    @GetMapping("/item/add")
    public ResponseEntity<ApiResponse> addItemToCart(
            @RequestParam Long productId,
            @RequestParam Integer qty
    ){
        try{
            User user = userService.getAuthenticatedUser();
            Cart cart = cartService.initializeNewCart(user);

            service.addCartItem(cart.getId(),productId,qty);
            return ResponseEntity.ok(
                    new ApiResponse(
                            "Add item:" + productId + " success",
                            null
                    )
            );
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(
                            new ApiResponse(
                                    e.getMessage(),
                                    null
                            )
                    );
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(
                            new ApiResponse(
                                e.getMessage(),
                                    null
                            )
                    );
        }
    }

    @DeleteMapping("/{cartId}/item/{productId}/remove")
    public ResponseEntity<ApiResponse> removeItemFromCart(
            @PathVariable Long cartId,
            @PathVariable Long productId
    ){
        Cart cart = new Cart();
        try{
            service.removeCartItemFromCart(cartId, productId);
            return ResponseEntity.ok(
                    new ApiResponse(
                            "product:" + productId + " successfully removed",
                            null
                    )
            );
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(
                            new ApiResponse(
                                    e.getMessage(),
                                    null
                            )
                    );
        }
    }

    @PutMapping("/cart/{cartId}/item/{productId}/update")
    public ResponseEntity<ApiResponse> updateItemQty(
            @PathVariable Long cartId,
            @PathVariable Long productId,
            @RequestParam Integer qty
    ){
        try {
            service.updateCartItemQty(cartId,productId,qty);
            return  ResponseEntity.ok(
                    new ApiResponse(
                            HttpStatus.OK.toString(),
                            null
                    )
            );
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(
                            new ApiResponse(
                                    e.getMessage(),
                                    null
                            )
                    );
        }
    }
}

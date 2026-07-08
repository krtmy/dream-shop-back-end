package com.helloworld.dreamshopsbackend.controller;

import com.helloworld.dreamshopsbackend.model.dto.OrderDto;
import com.helloworld.dreamshopsbackend.exception.category.ResourceNotFoundException;
import com.helloworld.dreamshopsbackend.model.Order;
import com.helloworld.dreamshopsbackend.response.ApiResponse;
import com.helloworld.dreamshopsbackend.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${order-api.prefix}")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService service;

    @PostMapping("/order")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> createOrder(@RequestParam Long userId) {
        try {
            Order order = service.placeOrder(userId);
            OrderDto convertedDto = service.convertToDto(order);
            return ResponseEntity.ok(
                    new ApiResponse(
                            HttpStatus.CREATED.toString(),
                            convertedDto
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(
                            new ApiResponse(
                                    HttpStatus.BAD_REQUEST.toString(),
                                    e.getMessage()
                            )
                    );
        }
    }

    @GetMapping("/get/order/{orderId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId) {
        try {
            OrderDto order = service.getOrder(orderId);
            return ResponseEntity.ok(
                    new ApiResponse(
                            HttpStatus.FOUND.toString(),
                            order
                    )
            );
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(
                            new ApiResponse(
                                    HttpStatus.NOT_FOUND.toString(),
                                    e.getMessage()
                            )
                    );
        }
    }

    @GetMapping("/get/{userId}/order")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId) {
        try {
            List<OrderDto> orders = service.getUserOrders(userId);
            return ResponseEntity.ok(
                    new ApiResponse(
                            HttpStatus.FOUND.toString(),
                            orders
                    )
            );
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(
                            new ApiResponse(
                                    HttpStatus.NOT_FOUND.toString(),
                                    e.getMessage()
                            )
                    );
        }
    }
}

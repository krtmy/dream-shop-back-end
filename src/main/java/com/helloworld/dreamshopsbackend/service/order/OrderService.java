package com.helloworld.dreamshopsbackend.service.order;

import com.helloworld.dreamshopsbackend.model.dto.OrderDto;
import com.helloworld.dreamshopsbackend.exception.category.ResourceNotFoundException;
import com.helloworld.dreamshopsbackend.model.*;
import com.helloworld.dreamshopsbackend.repository.ProductRepository;
import com.helloworld.dreamshopsbackend.repository.OrderRepository;
import com.helloworld.dreamshopsbackend.service.cart.CartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{

    private final OrderRepository repository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final ModelMapper mapper;

    @Transactional
    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItems = createOrderItem(order, cart);
        order.setOrderItems(new HashSet<>(orderItems));
        order.setTotalOrderAmount(calculateTotalPrice(orderItems));
        Order saveOrder = orderRepository.save(order);
        cartService.clearCart(cart.getId());

        return saveOrder;

    }

    private Order createOrder(Cart cart) {
        Order order = new Order();
        // set the user
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }

    private List<OrderItem> createOrderItem(Order order, Cart cart) {
        return cart.getCartItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            product.setInventory(product.getInventory() - cartItem.getQty());
            productRepository.save(product);
            return new OrderItem(
                    order,
                    product,
                    cartItem.getQty(),
                    cartItem.getUnitPrice()
            );
        }).toList();
    }

    private BigDecimal calculateTotalPrice(List<OrderItem> orderItemList) {

        return orderItemList.stream()
                .map(
                        item -> item.getPrice()
                                .multiply(
                                        new BigDecimal(item.getQty())
                                )
                ).reduce(BigDecimal.ZERO, BigDecimal::add);
    }



    @Override
    public OrderDto getOrder(Long orderId) {
        return repository.findById(orderId)
                .map(this::convertToDto)
                .orElseThrow(
                        () ->
                        new ResourceNotFoundException(
                                HttpStatus.NOT_FOUND.toString()
                        )
                );
    }

    @Override
    public List<OrderDto> getUserOrders(Long userId) {
        List<Order> orders = repository.findByUserId(userId);
        return orders.stream().map(
                this::convertToDto
        ).toList();
    }
    @Override
    public OrderDto convertToDto(Order order) {
        return mapper.map(order, OrderDto.class);
    }
}

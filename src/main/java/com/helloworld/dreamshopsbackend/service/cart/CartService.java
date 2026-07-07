package com.helloworld.dreamshopsbackend.service.cart;

import com.helloworld.dreamshopsbackend.exception.category.ResourceNotFoundException;
import com.helloworld.dreamshopsbackend.model.Cart;
import com.helloworld.dreamshopsbackend.model.User;
import com.helloworld.dreamshopsbackend.repository.CartItemRepository;
import com.helloworld.dreamshopsbackend.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService{
    private final CartRepository repository;
    private final CartItemRepository cartItemRepository;
    private final AtomicLong cartIdGeneration = new AtomicLong(0);


    @Override
    public Cart getCart(Long id) {
        Cart cart = repository.findById(id)
                .orElseThrow(
                        () ->new ResourceNotFoundException("cart:" + id + " not found")
                );
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return repository.save(cart);
    }

    @Transactional
    @Override
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        cartItemRepository.deleteAllByCartId(id);
        cart.getCartItems().clear();
        repository.deleteById(id);
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);
        return cart.getTotalAmount();
    }

    @Override
    public Cart initializeNewCart(User user) {
        return Optional.ofNullable(getCartByUserId(user.getId()))
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    return repository.save(cart);
                });
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return repository.findByUserId(userId);
    }
}

package com.helloworld.dreamshopsbackend.service.cart;

import com.helloworld.dreamshopsbackend.model.Cart;
import com.helloworld.dreamshopsbackend.model.User;

import java.math.BigDecimal;

public interface ICartService {

    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);

    Cart initializeNewCart(User user);

    Cart getCartByUserId(Long userId);
}

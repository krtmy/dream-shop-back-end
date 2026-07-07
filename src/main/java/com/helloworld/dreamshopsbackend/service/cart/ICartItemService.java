package com.helloworld.dreamshopsbackend.service.cart;

import com.helloworld.dreamshopsbackend.model.CartItem;

public interface ICartItemService {
    void addCartItem(Long cartId,Long productId, int qty);
    void removeCartItemFromCart(Long cartId, Long productId) ;
    void updateCartItemQty(Long cartId, Long productId, int qty);

    CartItem getCartItem(Long cartId, Long productId);
}

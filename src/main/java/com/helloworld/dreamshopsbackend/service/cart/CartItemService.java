package com.helloworld.dreamshopsbackend.service.cart;

import com.helloworld.dreamshopsbackend.exception.category.ResourceNotFoundException;
import com.helloworld.dreamshopsbackend.model.Cart;
import com.helloworld.dreamshopsbackend.model.CartItem;
import com.helloworld.dreamshopsbackend.model.Product;
import com.helloworld.dreamshopsbackend.repository.CartItemRepository;
import com.helloworld.dreamshopsbackend.repository.CartRepository;
import com.helloworld.dreamshopsbackend.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService{

    private final CartItemRepository repository;
    private final CartService cartService;
    private final IProductService productService;
    private final CartRepository cartRepository;

    @Override
    public void addCartItem(Long cartId, Long productId, int qty) {
        /*
        get cart first and then product
        check if product already exists in the cart
        if yes, then increase the qty with requested qty
        if no, initiate a new cartitem entry
         */
        Cart cart =  cartService.getCart(cartId);
        Product product = productService.getProductById(productId);
        CartItem cartItem =  cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().orElse(new CartItem());
        if (cartItem.getId() == null) {
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQty(qty);
            cartItem.setUnitPrice(product.getPrice());
        }else {
            cartItem.setQty(cartItem.getQty() + qty);
        }
        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        repository.save(cartItem);
        cartRepository.save(cart);
    }


    @Override
    public void removeCartItemFromCart(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        CartItem itemToRemove = getCartItem(cartId, productId);
        cart.removeItem(itemToRemove);
        cartRepository.save(cart);
    }

    @Override
    public void updateCartItemQty(Long cartId, Long productId, int qty) {

        Cart cart = cartService.getCart(cartId);
        cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(
                        item -> {
                            item.setQty(qty);
                            item.setUnitPrice(item.getProduct().getPrice());
                            item.setTotalPrice();
                        }
                );
        BigDecimal totalAmount = cart.getCartItems()
                        .stream()
                                .map(
                                        CartItem::getTotalPrice
                                ).reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalAmount(totalAmount);

        cartRepository.save(cart);

    }

    @Override
    public CartItem getCartItem(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        return cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));
    }
}

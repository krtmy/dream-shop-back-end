package com.helloworld.dreamshopsbackend.repository;

import com.helloworld.dreamshopsbackend.model.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart getCartById(Long id);

    Cart findByUserId(Long userId);
}

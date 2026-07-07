package com.helloworld.dreamshopsbackend.repository;

import com.helloworld.dreamshopsbackend.model.CartItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void deleteAllByCartId(Long id);
}

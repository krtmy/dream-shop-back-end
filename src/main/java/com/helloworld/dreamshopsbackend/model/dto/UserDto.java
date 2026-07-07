package com.helloworld.dreamshopsbackend.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.helloworld.dreamshopsbackend.model.Cart;
import com.helloworld.dreamshopsbackend.model.Order;
import com.helloworld.dreamshopsbackend.model.Role;
import lombok.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Collection<Role> roles = new HashSet<>();
//    @JsonIgnore
//    private String password;
    private Cart cart;
    private List<Order> orders;
}

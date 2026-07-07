package com.helloworld.dreamshopsbackend.model;


import jakarta.persistence.*;
import lombok.*;
import org.aspectj.weaver.ast.Or;
import org.hibernate.annotations.NaturalId;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @NaturalId
    private String email;
    private String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "user_roles",
        joinColumns = {
            @JoinColumn(name = "user_id", referencedColumnName = "id")
        },
    inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private Collection<Role> roles = new HashSet<>();

}

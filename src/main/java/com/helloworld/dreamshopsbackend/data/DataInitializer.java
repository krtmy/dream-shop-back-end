package com.helloworld.dreamshopsbackend.data;

import com.helloworld.dreamshopsbackend.model.Role;
import com.helloworld.dreamshopsbackend.model.User;
import com.helloworld.dreamshopsbackend.repository.RoleRepository;
import com.helloworld.dreamshopsbackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Transactional
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final RoleRepository roleRepository;
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Set<String> defaultRoles = Set.of("ROLE_USER", "ROLE_ADMIN");
        createDefaultRoleIfNotExist(defaultRoles);
        createDefaultAdminIfNotExist();
    }

    private void createDefaultAdminIfNotExist() {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN").get();
        String adminEmail = "ran41@gmail.com";
        String adminPassword = "admin";
        if (userRepository.existsByEmail(adminEmail)) {
            return;
        }
        User admin = new User();
        admin.setFirstName("Ran");
        admin.setLastName("41");
        admin.setEmail(adminEmail);
        admin.setRoles(Set.of(adminRole));
        admin.setPassword(encoder.encode(adminPassword));
        userRepository.save(admin);

    }

    private void createDefaultRoleIfNotExist(Set<String> defaultRoles) {
        defaultRoles.stream()
                .filter(role -> roleRepository.findByName(role).isEmpty())
                .map(Role::new)
                .forEach(roleRepository::save);

    }
}

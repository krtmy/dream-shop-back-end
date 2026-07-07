package com.helloworld.dreamshopsbackend.service.user;

import com.helloworld.dreamshopsbackend.model.Role;
import com.helloworld.dreamshopsbackend.model.dto.UserDto;
import com.helloworld.dreamshopsbackend.exception.category.ResourceAlreadyExistException;
import com.helloworld.dreamshopsbackend.exception.category.ResourceNotFoundException;
import com.helloworld.dreamshopsbackend.model.User;
import com.helloworld.dreamshopsbackend.repository.RoleRepository;
import com.helloworld.dreamshopsbackend.repository.UserRepository;
import com.helloworld.dreamshopsbackend.request.CreateUserRequest;
import com.helloworld.dreamshopsbackend.request.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final ModelMapper mapper;
    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final RoleRepository roleRepository;
    @Override
    public User getUserById(Long userId) {
        return repository.findById(userId)
                .orElseThrow(
                        ()-> new ResourceNotFoundException(
                                HttpStatus.NOT_FOUND.toString()
                        )
                );
    }

    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter(
                        user -> !repository.existsByEmail(request.getEmail())
                ).map(
                        req -> {
                            User user = new User();
                            user.setEmail(request.getEmail());
                            user.setPassword(encoder.encode(request.getPassword()));
                            user.setFirstName(request.getFirstName());
                            user.setLastName(request.getLastName());
                            Set<Role> userRoles = request.getRoles().stream()
                                    .map(roleName -> roleRepository.findByName(roleName)
                                            .orElseThrow(() -> new ResourceNotFoundException("Role: " + roleName + " not found")))
                                    .collect(Collectors.toSet());
                            if (userRoles.isEmpty()) {
                                Role defaultRole = roleRepository.findByName("ROLE_USER").get();
                                userRoles.add(defaultRole);
                            }
                            user.setRoles(userRoles);
                            return repository.save(user);
                        }
                ).orElseThrow(
                        () -> new ResourceAlreadyExistException(
                                "Oops " +request.getEmail() + " already existed"
                        )
                );
    }

    @Override
    public User updateUser(UserUpdateRequest request, Long userId) {
        return repository.findById(userId)
                .map(existingUser -> {
                    existingUser.setFirstName(request.getFirstName());
                    existingUser.setLastName(request.getLastName());
                    return repository.save(existingUser);
                }).orElseThrow(
                        () -> new ResourceNotFoundException(
                                HttpStatus.NOT_FOUND.toString()
                        )
                );
    }

    @Override
    public void deleteUser(Long userId) {
        repository.findById(userId)
                .ifPresentOrElse(
                        repository::delete,
                        () -> new ResourceNotFoundException(
                                HttpStatus.NOT_FOUND.toString()
                        )
                );
    }

    @Override
    public UserDto convertToUserDto(User user) {
        return mapper.map(user, UserDto.class);
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication=  SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();
        return repository.findByEmail(email);
    }
}

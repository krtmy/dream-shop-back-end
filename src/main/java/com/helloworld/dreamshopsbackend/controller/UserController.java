package com.helloworld.dreamshopsbackend.controller;


import com.helloworld.dreamshopsbackend.model.dto.UserDto;
import com.helloworld.dreamshopsbackend.exception.category.ResourceAlreadyExistException;
import com.helloworld.dreamshopsbackend.exception.category.ResourceNotFoundException;
import com.helloworld.dreamshopsbackend.model.User;
import com.helloworld.dreamshopsbackend.request.CreateUserRequest;
import com.helloworld.dreamshopsbackend.request.UserUpdateRequest;
import com.helloworld.dreamshopsbackend.response.ApiResponse;
import com.helloworld.dreamshopsbackend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${user-api.prefix}")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{userId}/user")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);
            UserDto updatedUserDto = userService.convertToUserDto(user);
            return ResponseEntity.ok(new ApiResponse("success", updatedUserDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse("error", e.getMessage())
            );
        }
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest createUserRequest) {
        try {
            User user = userService.createUser(createUserRequest);
            UserDto updatedUserDto = userService.convertToUserDto(user);
            return ResponseEntity.ok(new ApiResponse(
                    "User created successfully",
                    updatedUserDto
            ));
        } catch (ResourceAlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    new ApiResponse(
                            e.getMessage(),
                            null
                    )
            );
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update/{userId}")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UserUpdateRequest request, @PathVariable Long userId) {
        try {
            User user = userService.updateUser(request, userId);
            UserDto updatedUserDto = userService.convertToUserDto(user);
            return ResponseEntity.ok(new ApiResponse(
                    "User updated successfully",
                    updatedUserDto
            ));
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse(
                            e.getMessage(),
                            null
                    )
            );
        }
    }

    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok(new ApiResponse(
                    "User deleted successfully",
                    null
            ));
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse(
                            e.getMessage(),
                            null
                    )
            );
        }
    }
}

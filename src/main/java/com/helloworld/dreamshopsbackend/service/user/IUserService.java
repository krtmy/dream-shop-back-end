package com.helloworld.dreamshopsbackend.service.user;

import com.helloworld.dreamshopsbackend.model.dto.UserDto;
import com.helloworld.dreamshopsbackend.model.User;
import com.helloworld.dreamshopsbackend.request.CreateUserRequest;
import com.helloworld.dreamshopsbackend.request.UserUpdateRequest;

public interface IUserService {
    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UserUpdateRequest request, Long userId);
    void deleteUser(Long userId);

    UserDto convertToUserDto(User user);

    User getAuthenticatedUser();
}

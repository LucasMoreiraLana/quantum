package com.example.quantum.mappers;

import org.springframework.stereotype.Component;

import com.example.quantum.models.User;
import com.example.quantum.dtos.request.users.UserCreateRequest;
import com.example.quantum.dtos.request.users.UserUpdateRequest;
import com.example.quantum.dtos.response.users.UserResponse;

@Component
public class UserMapper {

    public User toEntity(UserCreateRequest request) {
        User user = new User();
        user.setUsername(request.username());
        user.setPassword(request.password());
        user.setEmail(request.email());
        user.setSector(request.sector());
        user.setPosition(request.position());
        user.setActive(true);
        return user;
    }

    public User updateEntity(User user, UserUpdateRequest request) {
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setSector(request.sector());
        user.setPosition(request.position());
        user.setActive(request.active());
        return user;
    }

    public UserResponse toDTO(User user) {
        return new UserResponse(
                user.getIdUser(),
                user.getUsername(),
                user.getEmail(),
                user.isActive(),
                user.getSector(),
                user.getPosition()
        );
    }
}

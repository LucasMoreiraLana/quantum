package com.example.quantum.mappers;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.quantum.dtos.users.UserCreateDTO;
import com.example.quantum.dtos.users.UserResponseDTO;
import com.example.quantum.dtos.users.UserUpdateDTO;
import com.example.quantum.models.User;

@Component
public class UserMapper {
    
    private final BCryptPasswordEncoder passwordEncoder;

    public UserMapper(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User toEntity(UserCreateDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setSector(dto.getSector());
        user.setPosition(dto.getPosition());
        user.setActive(true);
        return user;
    }

    public User updateEntity(User user, UserUpdateDTO dto) {
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setSector(dto.getSector());
        user.setPosition(dto.getPosition());
        user.setActive(dto.isActive());
        return user;
    }

    public UserResponseDTO toDTO(User user) {
        return UserResponseDTO.builder()
                .idUser(user.getIdUser())
                .username(user.getUsername())
                .email(user.getEmail())
                .active(user.isActive())
                .sector(user.getSector())
                .position(user.getPosition())
                .build();
    }
}

package com.example.quantum.services.user;

import com.example.quantum.repositories.user.UserEntityMapper;
import com.example.quantum.domain.User;
import com.example.quantum.repositories.user.UserEntity;
import com.example.quantum.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class GetAllUserGetService {

    @Autowired
    private UserRepository userRepository;


    public List<User> getAllUser() {
        return userRepository.findAll()
                .stream()
                .map(UserEntityMapper::toUser)
                .collect(Collectors.toList());
    }


}

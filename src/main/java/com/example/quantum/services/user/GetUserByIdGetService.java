package com.example.quantum.services.user;


import com.example.quantum.controllers.user.getuser.GetUserByIdGetMapper;
import com.example.quantum.domain.User;
import com.example.quantum.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetUserByIdGetService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> execute(GetUserByIdGetInput input){
        return userRepository.findByUserId(input.userId())
                .map(GetUserByIdGetMapper::toDomain);
    }
}

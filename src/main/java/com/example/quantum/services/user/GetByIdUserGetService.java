package com.example.quantum.services.user;


import com.example.quantum.controllers.user.getbyiduser.GetByIdUserGetMapper;
import com.example.quantum.domain.User;
import com.example.quantum.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetByIdUserGetService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> execute(GetByIdUserGetInput input){
        return userRepository.findByIdUser(input.idUser())
                .map(GetByIdUserGetMapper::toDomain);
    }
}

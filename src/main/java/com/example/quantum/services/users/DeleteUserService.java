package com.example.quantum.services.users;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import com.example.quantum.repositories.UserRepo;
import com.example.quantum.exceptions.UserNotFoundException;
import com.example.quantum.models.User;


@Service
@Transactional
@Validated
public class DeleteUserService {

    @Autowired
    private UserRepo userRepo;

    public void delete(UUID id) {
        User user = userRepo.findByIdUser(id)
            .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado: " + id));
        user.setActive(false);
        userRepo.save(user);
    }
}

package com.example.quantum.services.users;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import com.example.quantum.dtos.response.users.UserResponse;
import com.example.quantum.exceptions.UserNotFoundException;
import com.example.quantum.models.User;
import com.example.quantum.repositories.specs.users.UserSearchCriteria;
import com.example.quantum.repositories.specs.users.UserSpecs;
import com.example.quantum.repositories.UserRepo;
import com.example.quantum.mappers.UserMapper;


@Service
@Transactional
@Validated
public class GetUserService {
    
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserMapper userMapper;
    
    public UserResponse findById(UUID id) {
        User user = userRepo.findByIdUser(id)
            .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado: " + id));
        return userMapper.toDTO(user);
    }

    
    public List<UserResponse> findAllActive() {
        UserSearchCriteria criteria = UserSearchCriteria.builder()
            .active(true)
            .build();
        return userRepo.findAll(UserSpecs.withDynamicQuery(criteria))
            .stream()
            .map(userMapper::toDTO)
            .collect(Collectors.toList());
    }
}

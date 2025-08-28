package com.example.quantum.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import com.example.quantum.models.User;
import com.example.quantum.repositories.UserRepo;
import com.example.quantum.repositories.specs.UserSearchCriteria;
import com.example.quantum.repositories.specs.UserSpecs;
import jakarta.validation.Valid;
import com.example.quantum.exceptions.UserNotFoundException;
import com.example.quantum.mappers.UserMapper;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

import com.example.quantum.dtos.request.users.UserCreateRequest;
import com.example.quantum.dtos.request.users.UserUpdateRequest;
import com.example.quantum.dtos.response.users.UserResponse;



@Service
@Transactional
@Validated
public class UserService {
    
    @Autowired
    private final UserRepo userRepo;
    @Autowired
    private final UserMapper userMapper;

    public UserService(UserRepo userRepo, UserMapper userMapper) {
        this.userRepo = userRepo;
        this.userMapper = userMapper;
    }


    /**
     * Busca um usuario por ID
     */
    public UserResponse findById(UUID id) {
        User user = userRepo.findByIdUser(id)
            .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado: " + id));
        return userMapper.toDTO(user);
    }

    /**
     * Lista todos os usuários ativos
     */
    public List<UserResponse> findAllActive() {
        UserSearchCriteria criteria = UserSearchCriteria.builder()
            .onlyActive(true)
            .build();
        return userRepo.findAll(UserSpecs.withDynamicQuery(criteria))
            .stream()
            .map(userMapper::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Cria um novo usuário
     */
    public UserResponse create(@Valid UserCreateRequest createRequest) {
        User user = userMapper.toEntity(createRequest);
        user = userRepo.save(user);
        return userMapper.toDTO(user);
    }

    /**
     * Atualiza um usuário existente
     */
    public UserResponse update(UUID id, @Valid UserUpdateRequest updateRequest) {
        User user = userRepo.findByIdUser(id)
            .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado: " + id));
        
        user = userMapper.updateEntity(user, updateRequest);
        user = userRepo.save(user);
        return userMapper.toDTO(user);
    }

    /**
     * Exclusão lógica do documento
     */
    public void delete(UUID id) {
        User user = userRepo.findByIdUser(id)
            .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado: " + id));
        user.setActive(false);
        userRepo.save(user);
    }

    
}

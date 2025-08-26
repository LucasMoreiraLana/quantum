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

import com.example.quantum.dtos.users.UserCreateDTO;
import com.example.quantum.dtos.users.UserResponseDTO;
import com.example.quantum.dtos.users.UserUpdateDTO;
import com.example.quantum.exceptions.UserNotFoundException;
import com.example.quantum.mappers.UserMapper;

import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;



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
    public UserResponseDTO findById(UUID id) {
        User user = userRepo.findByIdUser(id)
            .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado: " + id));
        return userMapper.toDTO(user);
    }

    /**
     * Lista todos os usuários ativos
     */
    public List<UserResponseDTO> findAllActive() {
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
    public UserResponseDTO create(@Valid UserCreateDTO createDTO) {
        User user = userMapper.toEntity(createDTO);
        user = userRepo.save(user);
        return userMapper.toDTO(user);
    }

    /**
     * Atualiza um usuário existente
     */
    public UserResponseDTO update(UUID id, @Valid UserUpdateDTO updateDTO) {
        User user = userRepo.findByIdUser(id)
            .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado: " + id));
        
        user = userMapper.updateEntity(user, updateDTO);
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

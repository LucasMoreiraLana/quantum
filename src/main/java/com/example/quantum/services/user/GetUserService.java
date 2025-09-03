package com.example.quantum.services.user;

import com.example.quantum.domain.User;
import com.example.quantum.mappers.user.UserMapper;
import com.example.quantum.repositories.user.UserEntity;
import com.example.quantum.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class GetUserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> findAllUsers() {
        List<UserEntity> entities = userRepository.findAll();
        return entities.stream()
                       .map(UserMapper::toUser)
                       .collect(Collectors.toList());
    }


}

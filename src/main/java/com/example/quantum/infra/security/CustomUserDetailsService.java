package com.example.quantum.infra.security;

import com.example.quantum.repositories.user.UserEntity;
import com.example.quantum.repositories.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;

public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        UserEntity entity = this.userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User não encontrado!"));
        return new org.springframework.security.core.userdetails.User(entity.getEmail(), entity.getPassword(), new ArrayList<>());
    }

}

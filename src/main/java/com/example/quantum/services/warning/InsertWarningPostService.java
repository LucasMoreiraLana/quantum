package com.example.quantum.services.warning;

import com.example.quantum.controllers.warning.InsertWarningPostRequest;
import com.example.quantum.domain.Warning;
import com.example.quantum.repositories.user.UserRepository;
import com.example.quantum.repositories.warning.WarningEntityMapper;
import com.example.quantum.repositories.warning.WarningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InsertWarningPostService {

    @Autowired
    private WarningRepository warningRepository;

    @Autowired
    private UserRepository userRepository;



    public Warning createWarning(InsertWarningPostRequest request, String username){

        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado!"));

       return null;



    }
}

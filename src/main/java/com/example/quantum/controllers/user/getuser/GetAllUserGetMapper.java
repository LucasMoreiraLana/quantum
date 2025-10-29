package com.example.quantum.controllers.user.getuser;

import com.example.quantum.domain.User;

import java.util.List;
import java.util.stream.Collectors;

public class GetAllUserGetMapper {

    public static GetAllUserGetResponse toResponse(User user){
        return new GetAllUserGetResponse(
                user.userId(),
                user.username(),
                user.email(),
                user.active(),
                user.sector(),
                user.position()
        );
    }

    public static List<GetAllUserGetResponse> toResponseList(List<User> users){
        return users.stream()
                .map(GetAllUserGetMapper::toResponse)
                .collect(Collectors.toList());
    }
}

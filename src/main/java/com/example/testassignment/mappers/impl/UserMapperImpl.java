package com.example.testassignment.mappers.impl;

import com.example.testassignment.dtos.UpdateUserDTO;
import com.example.testassignment.dtos.UserDTO;
import com.example.testassignment.entities.User;
import com.example.testassignment.mappers.UserMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class UserMapperImpl implements UserMapper {

    private final static String api = "http://localhost:8080/api/user/";

    private final DateTimeFormatter dateFormatter = DateTimeFormatter
            .ofPattern("yyyy-MM-dd");

    @Override
    public UserDTO toDTO(User user) {
        return UserDTO.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .birthDate(user.getBirthDate().format(dateFormatter))
                .address(user.getAddress())
                .phoneNumber(user.getPhoneNumber())
                .selfLink(api + user.getId())
                .build();
    }

    @Override
    public User toEntity(UserDTO dto) {
        return User.builder()
                .email(dto.getEmail())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .birthDate(LocalDate.parse(dto.getBirthDate(), dateFormatter))
                .address(dto.getAddress())
                .phoneNumber(dto.getPhoneNumber())
                .build();
    }
}

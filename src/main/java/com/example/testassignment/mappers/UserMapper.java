package com.example.testassignment.mappers;

import com.example.testassignment.dtos.UpdateUserDTO;
import com.example.testassignment.dtos.UserDTO;
import com.example.testassignment.entities.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    UserDTO toDTO(User user);

    User toEntity(UserDTO dto);

}

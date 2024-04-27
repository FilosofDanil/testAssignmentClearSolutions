package com.example.testassignment.services;

import com.example.testassignment.dtos.UpdateUserDTO;
import com.example.testassignment.dtos.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> getAll();

    List<UserDTO> searchByBirthDate(String dateFrom, String dateTo);

    UserDTO createUser(UserDTO userDTO);

    void updateUser(UpdateUserDTO updateUserDTO, Long id);

    void deleteUser(Long id);

}

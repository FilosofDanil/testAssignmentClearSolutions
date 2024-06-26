package com.example.testassignment.services.impl;

import com.example.testassignment.config.MinimalAgeConfiguration;
import com.example.testassignment.dtos.UpdateUserDTO;
import com.example.testassignment.dtos.UserDTO;
import com.example.testassignment.entities.User;
import com.example.testassignment.exceptions.InvalidDateException;
import com.example.testassignment.exceptions.NoContentException;
import com.example.testassignment.exceptions.ResourceNotFoundException;
import com.example.testassignment.mappers.UserMapper;
import com.example.testassignment.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @Mock
    MinimalAgeConfiguration ageConfiguration;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    void getAll() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        List<UserDTO> result = userService.getAll();
        verify(userRepository, times(1)).findAll();
        assertEquals(0, result.size());
    }

    @Test
    void searchByBirthDate() {
        LocalDate fromDate = LocalDate.now().minusYears(30);
        LocalDate toDate = LocalDate.now().minusYears(20);
        when(userRepository.findUsersByBirthDateBetween(fromDate, toDate)).thenReturn(Collections.emptyList());
        List<UserDTO> result = userService.searchByBirthDate(fromDate.toString(), toDate.toString());
        verify(userRepository, times(1)).findUsersByBirthDateBetween(fromDate, toDate);
        assertEquals(0, result.size());
    }

    @Test
    void createUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("Danylo");
        userDTO.setLastName("Berk");
        userDTO.setEmail("Danylo.Berk.example.com");
        userDTO.setBirthDate(LocalDate.now().minusYears(25).toString());
        User user = User.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .birthDate(LocalDate.parse(userDTO.getBirthDate()))
                .build();
        when(userMapper.toEntity(userDTO)).thenReturn(user);
        when(userRepository.save(any())).thenReturn(user);
        when(userMapper.toDTO(any())).thenReturn(userDTO);
        UserDTO result = userService.createUser(userDTO);
        verify(userRepository, times(1)).save(user);
        assertEquals(userDTO, result);
    }

    @Test
    void updateUser() {
        Long userId = 1L;
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setFirstName("Danylo");
        updateUserDTO.setLastName("Berk");
        updateUserDTO.setEmail("Danylo.Berk.example.com");
        updateUserDTO.setBirthDate(LocalDate.now().minusYears(25).toString());
        when(userRepository.existsById(userId)).thenReturn(true);
        when(ageConfiguration.getMinimalAge()).thenReturn(18);
        userService.updateUser(updateUserDTO, userId);
        verify(userRepository, times(1))
                .updateUser(userId, updateUserDTO.getFirstName(), updateUserDTO.getLastName(),
                        updateUserDTO.getEmail(), updateUserDTO.getPhoneNumber(), updateUserDTO.getBirthDate(), updateUserDTO.getAddress());
    }

    @Test
    void deleteUser() {
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);
        userService.deleteUser(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }


    @Test
    void createUserWithInvalidAge() {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("Danylo");
        userDTO.setLastName("Berk");
        userDTO.setEmail("Danylo.Berk.example.com");
        userDTO.setBirthDate(LocalDate.now().minusYears(16).toString());

        assertThrows(Exception.class, () -> userService.createUser(userDTO));
        verify(userRepository, never()).save(any());
    }

    @Test
    void createUserWithInvalidData() {
        UserDTO userDTO = new UserDTO();
        userDTO.setLastName("Berk");
        userDTO.setEmail("Danylo.Berk.example.com");

        assertThrows(Exception.class, () -> userService.createUser(userDTO));
        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteUserNoContent() {
        Long userId = 1L;

        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(NoContentException.class, () -> userService.deleteUser(userId));
        verify(userRepository, never()).deleteById(userId);
    }

    @Test
    void updateUserNotFound() {
        Long userId = 1L;
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setFirstName("Danylo");
        updateUserDTO.setLastName("Berk");
        updateUserDTO.setEmail("Danylo.Berk.example.com");
        updateUserDTO.setBirthDate(LocalDate.now().minusYears(25).toString());

        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, ()
                -> userService.updateUser(updateUserDTO, userId));
        verify(userRepository, never()).save(any());
    }
}
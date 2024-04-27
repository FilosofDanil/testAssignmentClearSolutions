package com.example.testassignment.controllers;

import com.example.testassignment.dtos.UpdateUserDTO;
import com.example.testassignment.dtos.UserDTO;
import com.example.testassignment.exceptions.InvalidDateException;
import com.example.testassignment.exceptions.NoContentException;
import com.example.testassignment.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void getAllUsers() throws Exception {
        when(userService.getAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/user"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getAllUsersByBirthDate() throws Exception {
        LocalDate dateFrom = LocalDate.now().minusYears(30);
        LocalDate dateTo = LocalDate.now().minusYears(20);
        when(userService.searchByBirthDate(anyString(), anyString()))
                .thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/user/search")
                        .param("dateFrom", dateFrom.toString())
                        .param("dateTo", dateTo.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void createUser() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("Danylo");
        userDTO.setLastName("Berk");
        userDTO.setEmail("Danylo.Berk@example.com");
        userDTO.setBirthDate(LocalDate.now().minusYears(25).toString());
        when(userService.createUser(any(UserDTO.class))).thenReturn(userDTO);
        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"firstName\": \"Danylo\", \"lastName\": \"Berk\", \"email\": \"Danylo.Berk@example.com\", \"birthDate\": \"1996-01-01\" }"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value(userDTO.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(userDTO.getLastName()))
                .andExpect(jsonPath("$.email").value(userDTO.getEmail()))
                .andExpect(jsonPath("$.birthDate").value(userDTO.getBirthDate()));
    }

    @Test
    void updateUser() throws Exception {
        Long userId = 1L;
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setFirstName("Danylo");
        updateUserDTO.setLastName("Berk");
        updateUserDTO.setEmail("Danylo.Berk@example.com");
        updateUserDTO.setBirthDate(LocalDate.now().minusYears(25).toString());
        doNothing().when(userService).updateUser(eq(updateUserDTO), eq(userId));
        mockMvc.perform(put("/api/user/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"firstName\": \"Danylo\", \"lastName\": \"Berk\", \"email\": \"Danylo.Berk@example.com\", \"birthDate\": \"1996-01-01\" }"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUser() throws Exception {
        Long userId = 1L;
        mockMvc.perform(delete("/api/user/{id}", userId))
                .andExpect(status().isOk());
    }

    @Test
    void createUserWithInvalidData() throws Exception {
        String invalidEmail = "Danylo.Berk.example.com";
        String requestBody = "{ \"firstName\": \"Danylo\", \"lastName\": \"Berk\", \"email\": \"" + invalidEmail + "\", \"birthDate\": \"1996-01-01\" }";
        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
        requestBody = "{ \"lastName\": \"Berk\", \"email\": \"Danylo.Berk@example.com\" }";
        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUserWithInvalidAge() throws Exception {
        LocalDate invalidBirthDate = LocalDate.now().minusYears(16);
        String requestBody = "{ \"firstName\": \"Danylo\", \"lastName\": \"Berk\", \"email\": \"Danylo.Berk@example.com\", \"birthDate\": \"" + invalidBirthDate + "\" }";
        when(userService.createUser(any()))
                .thenThrow(new InvalidDateException(""));
        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUserWithInvalidEmail() throws Exception {
        Long userId = 1L;
        String invalidEmail = "failure.failure.fail.com";
        String requestBody = "{ \"firstName\": \"Danylo\", \"lastName\": \"Loh\", \"email\": \"" + invalidEmail + "\", \"birthDate\": \"1996-01-01\" }";
        mockMvc.perform(put("/api/user/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
        requestBody = "{ \"lastName\": \"Loh\", \"email\": \"failure.failure.fail.com\" }";
        mockMvc.perform(put("/api/user/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteUserWithNoContent() throws Exception {
        Long userId = 1L;
        doThrow(new NoContentException(""))
                .when(userService).deleteUser(userId);
        mockMvc.perform(delete("/api/user/{id}", userId))
                .andExpect(status().isNoContent());
    }
}
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
import com.example.testassignment.services.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;

    UserMapper userMapper;

    MinimalAgeConfiguration ageConfiguration;

    @Override
    public List<UserDTO> getAll() {
        log.info("Getting all users");
        return userRepository
                .findAll()
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

    @Override
    public List<UserDTO> searchByBirthDate(String dateFrom, String dateTo) {
        log.info("Getting users by date of birth");
        LocalDate fromDate = LocalDate.parse(dateFrom);
        LocalDate toDate = LocalDate.parse(dateTo);
        if (!fromDate.isBefore(toDate)) {
            throw new InvalidDateException("Starting date should be before ending date");
        }
        return userRepository.findUsersByBirthDateBetween(fromDate, toDate)
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        log.info("Creating new user");
        User user = userMapper.toEntity(userDTO);
        if (isUnderAge(user.getBirthDate())){
            log.warn("Client under 18 years old tried to register");
            throw new InvalidDateException("User should be at least 18 years old!");
        }
        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    @Override
    @Transactional
    public void updateUser(UpdateUserDTO updateUserDTO, Long id) {
        log.info("Updating user with id: " + id);
        if (!userRepository.existsById(id)) {
            log.warn("User not found with id: {}", id);
            throw new ResourceNotFoundException("User not found with id " + id);
        }
        LocalDate birthDate = null;
        if(updateUserDTO.getBirthDate()!=null){
            birthDate = LocalDate.parse(updateUserDTO.getBirthDate());
            if (isUnderAge(birthDate)){
                log.warn("Client tried set invalid birth date");
                throw new InvalidDateException("User should be at least 18 years old!");
            }
        }
        userRepository.updateUser(id, updateUserDTO.getFirstName(), updateUserDTO.getLastName(),
                updateUserDTO.getEmail(), updateUserDTO.getPhoneNumber(), updateUserDTO.getBirthDate(), updateUserDTO.getAddress());
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Deleting user with id: " + id);
        if (!userRepository.existsById(id)) {
            log.warn("Client tried to delete user with id" + id + ", but such user is not exist.");
            throw new NoContentException("User with this id is not present in the database. Deletion aborted");
        }
        userRepository.deleteById(id);
    }

    private boolean isUnderAge(LocalDate birthDate) {
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate, currentDate).getYears() < ageConfiguration.getMinimalAge();
    }
}

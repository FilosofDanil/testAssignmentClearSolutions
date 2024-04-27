package com.example.testassignment.repositories;

import com.example.testassignment.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findUsersByBirthDateBetween(LocalDate dateFrom, LocalDate dateTo);

    @Modifying
    @Query(value = "UPDATE users SET name = CASE WHEN :name IS NOT NULL THEN :name ELSE name END," +
            "    surname = CASE WHEN :surname IS NOT NULL THEN :surname ELSE surname END," +
            "    email = CASE WHEN :email IS NOT NULL THEN :email ELSE email END," +
            "    birth_date = CASE WHEN :birthDate IS NOT NULL THEN CAST(:birthDate AS DATE) ELSE birth_date END," +
            "    phone_number = CASE WHEN :phoneNumber IS NOT NULL THEN :phoneNumber ELSE phone_number END," +
            "    address = CASE WHEN :address IS NOT NULL THEN :address ELSE address END" +
            " WHERE id = :id",
            nativeQuery = true)
    void updateUser(@Param("id") Long id,
                    @Param("name") String name,
                    @Param("surname") String surname,
                    @Param("email") String email,
                    @Param("phoneNumber") String phoneNumber,
                    @Param("birthDate") String birthDate,
                    @Param("address") String address);

}

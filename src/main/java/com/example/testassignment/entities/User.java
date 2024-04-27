package com.example.testassignment.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    Long id;
    @Column(name = "name", nullable = false)
    @NotBlank(message = "Name should be not blank")
    String firstName;

    @Column(name = "surname", nullable = false)
    @NotBlank(message = "Last name should be not blank")
    String lastName;

    @Column(name = "email", nullable = false, unique = true)
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should match the format")
    String email;

    @Column(name = "birth_date", nullable = false, unique = true)
    @Past(message = "Invalid date")
    LocalDate birthDate;

    @Column(name = "phone_number", unique = true)
    @Pattern(regexp = "(^$|\\+380[0-9]{9})", message = "Phone number should be valid")
    String phoneNumber;

    @Column(name = "address")
    String address;
}

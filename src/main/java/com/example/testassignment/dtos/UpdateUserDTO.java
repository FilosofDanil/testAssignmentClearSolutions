package com.example.testassignment.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUserDTO {
    @Email(message = "Email should match the format")
    String email;
    String firstName;
    String lastName;
    String birthDate;
    String address;
    @Pattern(regexp = "(^$|\\+380[0-9]{9})", message = "Phone number should be valid")
    String phoneNumber;
}

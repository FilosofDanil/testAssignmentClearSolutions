package com.example.testassignment.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should match the format")
    String email;
    @NotBlank(message = "Name should be not blank")
    String firstName;
    @NotBlank(message = "Last name should be not blank")
    String lastName;
    @NotBlank(message = "Birth date is mandatory")
    String birthDate;
    String address;
    @Pattern(regexp = "(^$|\\+380[0-9]{9})", message = "Phone number should be valid")
    String phoneNumber;
    String selfLink;
}

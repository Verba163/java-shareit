package ru.practicum.shareit.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    Long id;

    @NotBlank(message = "Name cannot be empty")
    String name;

    @NotBlank(message = "Email must not be empty")
    @Email(message = "Invalid Email")
    String email;
}

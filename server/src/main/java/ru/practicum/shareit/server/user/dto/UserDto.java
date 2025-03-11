package ru.practicum.shareit.server.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    Long id;

    String name;

    @Email(message = "Invalid Email")
    @NotBlank(message = "Can not be empty")
    String email;
}

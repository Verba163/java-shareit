package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * TODO Sprint add-controllers.
 */

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Item {

    Long id;

    @NotBlank(message = "Name can not be empty")
    String name;

    @NotBlank(message = "Description can not be empty")
    String description;

    Long ownerId;

    @NotNull(message = "Available can not be empty")
    Boolean available;
}

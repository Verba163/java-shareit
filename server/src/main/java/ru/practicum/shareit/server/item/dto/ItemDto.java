package ru.practicum.shareit.server.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {

    Long id;

    @NotBlank(message = "Not able to add item with no name")
    String name;

    @NotBlank(message = "Not able to add item with no description")
    String description;

    Long ownerId;

    @NotBlank(message = "Not able to add item with no available")
    Boolean available;

    Long requestId;
}
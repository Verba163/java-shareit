package ru.practicum.shareit.gateway.request.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.gateway.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class ItemRequestDto {

    Long id;

    @NotBlank(message = "Description can not be empty")
    @Size(max = 255, message = "Description can not be more then 255 symbols")
    String description;

    Long requester;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<ItemDto> items;

    LocalDateTime created;
}

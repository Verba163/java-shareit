package ru.practicum.shareit.gateway.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentsDto {

    Long id;

    @NotBlank(message = "Not able to add comment with no text")
    String text;

    Long itemId;
    String authorName;
    Timestamp created;
}



package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.model.Booking;

import java.util.Set;

/**
 * TODO Sprint add-controllers.
 */

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "Name can not be empty")
    String name;

    @NotBlank(message = "Description can not be empty")
    String description;

    Long ownerId;

    @NotNull(message = "Available can not be empty")
    Boolean available;

    @OneToMany(mappedBy = "item")
    Set<Comments> comments;

    @OneToMany(mappedBy = "item")
    Set<Booking> bookings;
}

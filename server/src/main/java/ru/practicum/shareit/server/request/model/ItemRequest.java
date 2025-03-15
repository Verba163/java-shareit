package ru.practicum.shareit.server.request.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.server.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@EqualsAndHashCode
@Table(name = "requests")
@Data
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Size(max = 255, message = "Description can not be more then 255 symbols")
    String description;

    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    User requester;

    @Column(nullable = false)
    LocalDateTime created;
}

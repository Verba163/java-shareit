package ru.practicum.shareit.server.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.server.item.model.Comments;

public interface CommentsRepository extends JpaRepository<Comments, Long> {
}